package com.echsylon.kraken;

import android.net.Uri;

import com.echsylon.blocks.callback.DefaultRequest;
import com.echsylon.blocks.callback.Request;
import com.echsylon.blocks.network.NetworkClient;
import com.echsylon.blocks.network.OkHttpNetworkClient;
import com.echsylon.kraken.exception.KrakenRequestException;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.echsylon.kraken.Utils.asBytes;
import static com.echsylon.kraken.Utils.asString;
import static com.echsylon.kraken.Utils.base64Encode;
import static com.echsylon.kraken.Utils.concat;
import static com.echsylon.kraken.Utils.hmacSha512;
import static com.echsylon.kraken.Utils.sha256;

/**
 * This class knows how to prepare a request for client side caching. The super
 * API offers means of configuring client side soft and hard caching.
 * <p>
 * A soft cache means that the cache metrics provided by the caller will only be
 * applied if the server itself didn't provide any cache metrics.
 * <p>
 * Similarly a hard cache means that the caller provided cache metrics will
 * override any cache metrics provided by the server.
 * <p>
 * NOTE! Implementing classes MUST use the {@code okHttpNetworkClient}, provided
 * by the super class as a protected field, when implementing {@code enqueue()}
 * in order to make use of any cache configuration made by the caller.
 */
abstract class RequestBuilder<T> extends OkHttpNetworkClient.CachedRequestBuilder<RequestBuilder<T>> {

    /**
     * INTERNAL USE ONLY!
     * <p>
     * This class describes a response to a Kraken request. Note that Kraken
     * doesn't necessarily respond with HTTP error status codes if something
     * goes wrong.
     */
    private static final class Response<T> {
        @SerializedName("error")
        private String[] error;
        @SerializedName("result")
        private T result;
    }


    private final int cost;
    private final Type typeOfResult;
    private final CallCounter callCounter;

    private final String key;
    private final byte[] secret;

    private final String method;
    private final String baseUrl;
    private final String path;

    protected final HashMap<String, String> data;


    RequestBuilder(final int cost,
                   final CallCounter callCounter,
                   final String key,
                   final byte[] secret,
                   final String baseUrl,
                   final String method,
                   final String path,
                   final Type typeOfResult) {

        this.cost = cost;
        this.callCounter = callCounter;
        this.key = key;
        this.secret = secret;
        this.baseUrl = baseUrl;
        this.method = method;
        this.path = path;
        this.typeOfResult = typeOfResult;
        this.data = new HashMap<>();
    }


    /**
     * Creates and enqueues the actual request.
     *
     * @return A request object to attach any callback implementations to.
     */
    public Request<T> enqueue() {
        return new DefaultRequest<>(() -> {
            // Ensure we don't exceed our call rate limit.
            if (callCounter != null)
                callCounter.allocate(cost);

            // Perform coarse API key+secret validation
            if (isPrivateRequest(path) && (key == null || secret == null))
                throw new IllegalStateException(
                        "An API key and secret is required for this request");

            // Everything's peachy: prepare the Kraken request decoration
            String nonce = generateNonce(path);
            String message = composeMessage(nonce, data);
            List<NetworkClient.Header> headers = generateSignature(path, nonce, message);

            // Prepare the HTTP metrics depending on "GET" or "POST" method.
            boolean isGetMethod = "GET".equals(method);
            byte[] payload = isGetMethod ? null : asBytes(message);
            String mime = isGetMethod ? null : "application/x-www-form-urlencoded";
            String uri = isGetMethod && message != null ?
                    String.format("%s%s?%s", baseUrl, path, message) :
                    String.format("%s%s", baseUrl, path);

            // Perform the actual request (the network client stems from
            // CachedRequestBuilder (extended by RequestBuilder).
            byte[] responseBytes = okHttpNetworkClient.execute(uri, method, headers, payload, mime);
            String responseJson = asString(responseBytes);
            Type type = TypeToken.getParameterized(Response.class, typeOfResult).getType();
            Response<T> response = new GsonBuilder()
                    .registerTypeAdapterFactory(new KrakenTypeAdapterFactory())
                    .create()
                    .fromJson(responseJson, type);


            // Throw exception if has error (to trigger error callbacks)...
            if (response.error != null && response.error.length > 0)
                throw new KrakenRequestException(response.error);

            // ...or deliver result.
            return response.result;
        });
    }


    /**
     * Checks whether a given url is targeting a private endpoint in the Kraken
     * API.
     *
     * @param url The url to check.
     * @return Boolean true if targeting a private endpoint, false otherwise.
     */
    private boolean isPrivateRequest(final String url) {
        return url.contains("/private/");
    }

    /**
     * Generates a nonce for private requests, or null for public requests.
     *
     * @return The nonce or null.
     */
    private String generateNonce(final String url) {
        return url != null && isPrivateRequest(url) ?
                String.format("%-16s", System.currentTimeMillis())
                        .replace(" ", "0") :
                null;
    }

    /**
     * Constructs an encoded request message that Kraken will understand. See
     * Kraken API documentation for details.
     *
     * @param nonce The request nonce. Ignored if null.
     * @param data  The actual key value pairs to build the message from. Even
     *              positions are treated as keys and odd positions as values.
     *              Any null pointer key or value will render the key/value pair
     *              invalid and hence ignored. Any trailing single keys will
     *              also be ignored.
     * @return The prepared and encoded Kraken message.
     */
    private String composeMessage(final String nonce,
                                  final HashMap<String, String> data) {

        Uri.Builder builder = new Uri.Builder();

        if (nonce != null)
            builder.appendQueryParameter("nonce", nonce);

        if (data != null && !data.isEmpty())
            for (Map.Entry<String, String> entry : data.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key != null && value != null)
                    builder.appendQueryParameter(key, value);
            }

        return builder.build().getEncodedQuery();
    }

    /**
     * Generates the message signature headers. See Kraken API documentation for
     * details.
     *
     * @param path    The request path.
     * @param nonce   The request nonce that was used for the message.
     * @param message The request message
     * @return The Kraken request signature headers. Null if no path is given or
     * an empty list if no nonce is given.
     */
    private List<NetworkClient.Header> generateSignature(final String path,
                                                         final String nonce,
                                                         final String message) {
        if (key == null || secret == null)
            return null;

        if (path == null)
            return null;

        if (nonce == null)
            return null;

        byte[] data = sha256(asBytes(nonce + message));
        if (data == null)
            return null;

        byte[] input = concat(asBytes(path), data);
        byte[] signature = hmacSha512(input, secret);
        if (signature == null)
            return null;

        List<NetworkClient.Header> headers = new ArrayList<>(2);
        headers.add(new NetworkClient.Header("API-Key", key));
        headers.add(new NetworkClient.Header("API-Sign", base64Encode(signature)));

        return headers;
    }

}
