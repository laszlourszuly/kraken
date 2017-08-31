package com.echsylon.kraken.request;

import com.echsylon.kraken.Dictionary;
import com.echsylon.kraken.dto.Ticker;
import com.echsylon.kraken.internal.CallCounter;
import com.google.gson.reflect.TypeToken;

import static com.echsylon.kraken.internal.Utils.join;

/**
 * This class can build a request that retrieves information about the ticker
 * state for tradable asset pairs.
 * <p>
 * For further technical details see Kraken API documentation at:
 * https://www.kraken.com/help/api
 */
@SuppressWarnings("WeakerAccess")
public class TickerInfoRequestBuilder extends RequestBuilder<Dictionary<Ticker>> {

    /**
     * Constructor, intentionally hidden from public use.
     *
     * @param callCounter The request call counter. May be null.
     * @param baseUrl     The base url of the request.
     * @param key         The user API key.
     * @param secret      The corresponding secret.
     */
    public TickerInfoRequestBuilder(final CallCounter callCounter,
                                    final String baseUrl,
                                    final String key,
                                    final byte[] secret) {

        super(1, callCounter, key, secret, baseUrl,
                "GET", "/0/public/Ticker",
                TypeToken.getParameterized(
                        Dictionary.class,
                        Ticker.class).getType());
    }

    /**
     * Sets the asset pairs request property.
     *
     * @param pairs The asset pairs to get information on.
     * @return This request builder instance allowing method call chaining.
     */
    public TickerInfoRequestBuilder useAssetPairs(String... pairs) {
        data.put("pair", join(pairs));
        return this;
    }

}
