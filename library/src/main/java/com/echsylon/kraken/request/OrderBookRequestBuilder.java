package com.echsylon.kraken.request;

import com.echsylon.kraken.Dictionary;
import com.echsylon.kraken.dto.Depth;
import com.echsylon.kraken.internal.CallCounter;
import com.google.gson.reflect.TypeToken;

import static com.echsylon.kraken.internal.Utils.asString;

/**
 * This class can build a request that retrieves the market depth for tradable
 * asset pairs.
 * <p>
 * For further technical details see Kraken API documentation at:
 * https://www.kraken.com/help/api
 */
@SuppressWarnings("WeakerAccess")
public class OrderBookRequestBuilder extends RequestBuilder<Dictionary<Depth>> {

    /**
     * Creates a new request builder.
     *
     * @param callCounter The request call counter. May be null.
     * @param baseUrl     The base url of the request.
     * @param key         The user API key.
     * @param secret      The corresponding secret.
     */
    public OrderBookRequestBuilder(final CallCounter callCounter,
                                   final String baseUrl,
                                   final String key,
                                   final byte[] secret) {

        super(1, callCounter, key, secret, baseUrl,
                "GET", "/0/public/Depth",
                TypeToken.getParameterized(
                        Dictionary.class,
                        Depth.class).getType());
    }

    /**
     * Sets the asset pair request property.
     *
     * @param pair The asset pair to get the market depth for.
     * @return This request builder instance allowing method call chaining.
     */
    public OrderBookRequestBuilder useAssetPair(String pair) {
        data.put("pair", pair);
        return this;
    }

    /**
     * Sets the maximum asks/bids count request property.
     *
     * @param count The maximum number of asks or bids.
     * @return This request builder instance allowing method call chaining.
     */
    public OrderBookRequestBuilder useCount(int count) {
        data.put("count", asString(count));
        return this;
    }

}
