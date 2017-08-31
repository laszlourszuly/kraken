package com.echsylon.kraken.request;

import com.echsylon.kraken.Dictionary;
import com.echsylon.kraken.dto.Spread;
import com.echsylon.kraken.internal.CallCounter;
import com.google.gson.reflect.TypeToken;

/**
 * This class can build a request that retrieves information on recent spread
 * data for a tradable asset pair.
 * <p>
 * For further technical details see Kraken API documentation at:
 * https://www.kraken.com/help/api
 */
@SuppressWarnings("WeakerAccess")
public class RecentSpreadRequestBuilder extends RequestBuilder<Dictionary<Spread[]>> {

    /**
     * Constructor, intentionally hidden from public use.
     *
     * @param callCounter The request call counter. May be null.
     * @param baseUrl     The base url of the request.
     * @param key         The user API key.
     * @param secret      The corresponding secret.
     */
    public RecentSpreadRequestBuilder(final CallCounter callCounter,
                                      final String baseUrl,
                                      final String key,
                                      final byte[] secret) {

        super(1, callCounter, key, secret, baseUrl,
                "GET", "/0/public/Spread",
                TypeToken.getParameterized(
                        Dictionary.class,
                        Spread[].class).getType());
    }

    /**
     * Sets the asset pair request property.
     *
     * @param pair The asset pair to get recent spread data for.
     * @return This request builder instance allowing method call chaining.
     */
    public RecentSpreadRequestBuilder useAssetPair(String pair) {
        data.put("pair", pair);
        return this;
    }

    /**
     * Sets the since id request property.
     *
     * @param sinceId The spread id of the previous poll. Inclusive.
     * @return This request builder instance allowing method call chaining.
     */
    public RecentSpreadRequestBuilder useSinceId(String sinceId) {
        data.put("since", sinceId);
        return this;
    }

}
