package com.echsylon.kraken.request;

import com.echsylon.blocks.callback.DefaultRequest;
import com.echsylon.blocks.callback.Request;
import com.echsylon.blocks.network.NetworkClient;
import com.echsylon.blocks.network.OkHttpNetworkClient;
import com.echsylon.kraken.KrakenRequestException;
import com.echsylon.kraken.internal.CallCounter;
import com.echsylon.kraken.internal.KrakenTypeAdapterFactory;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import static com.echsylon.kraken.internal.Utils.asBytes;
import static com.echsylon.kraken.internal.Utils.asString;
import static com.echsylon.kraken.internal.Utils.composeMessage;
import static com.echsylon.kraken.internal.Utils.generateNonce;
import static com.echsylon.kraken.internal.Utils.generateSignatureHeaders;
import static com.echsylon.kraken.internal.Utils.isPrivateRequest;

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
public abstract class RequestBuilder<T> extends OkHttpNetworkClient.CachedRequestBuilder<RequestBuilder<T>> {

    /**
     * INTERNAL USE ONLY!
     * <p>
     * This class describes a response to a Kraken request. Note that Kraken
     * doesn't necessarily respond with HTTP error status codes if something
     * goes wrong.
     */
    @SuppressWarnings("unused")
    private static final class Response<T> {
        @SerializedName("error")
        private String[] error;
        @SerializedName("result")
        private T result;
    }

    protected final HashMap<String, String> data;
    private final String method;
    private final String baseUrl;
    private final String path;

    private final int cost;
    private final String key;
    private final byte[] secret;
    private final Type typeOfResult;
    private final CallCounter callCounter;


    protected RequestBuilder(final int cost,
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
            List<NetworkClient.Header> headers = generateSignatureHeaders(key, secret, path, nonce, message);

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


}
