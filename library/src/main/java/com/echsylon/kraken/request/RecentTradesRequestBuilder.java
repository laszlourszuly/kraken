package com.echsylon.kraken.request;

import com.echsylon.kraken.Dictionary;
import com.echsylon.kraken.dto.Trade;
import com.echsylon.kraken.internal.CallCounter;
import com.google.gson.reflect.TypeToken;

/**
 * This class can build a request that retrieves information on recent trades on
 * the market.
 * <p>
 * For further technical details see Kraken API documentation at:
 * https://www.kraken.com/help/api
 */
@SuppressWarnings("WeakerAccess")
public class RecentTradesRequestBuilder extends RequestBuilder<Dictionary<Trade[]>> {

    /**
     * Creates a new request builder.
     *
     * @param callCounter The request call counter. May be null.
     * @param baseUrl     The base url of the request.
     * @param key         The user API key.
     * @param secret      The corresponding secret.
     */
    public RecentTradesRequestBuilder(final CallCounter callCounter,
                                      final String baseUrl,
                                      final String key,
                                      final byte[] secret) {

        super(1, callCounter, key, secret, baseUrl,
                "GET", "/0/public/Trades",
                TypeToken.getParameterized(
                        Dictionary.class,
                        Trade[].class).getType());
    }

    /**
     * Sets the asset pair request property.
     *
     * @param pair The asset pair to get recent trades for.
     * @return This request builder instance allowing method call chaining.
     */
    public RecentTradesRequestBuilder useAssetPair(final String pair) {
        data.put("pair", pair);
        return this;
    }

    /**
     * Sets the since id request property.
     *
     * @param sinceId The trade id of the previous poll. Exclusive.
     * @return This request builder instance allowing method call chaining.
     */
    public RecentTradesRequestBuilder useSinceId(final String sinceId) {
        data.put("since", sinceId);
        return this;
    }

}
