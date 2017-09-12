package com.echsylon.kraken.request;

import com.echsylon.kraken.Dictionary;
import com.echsylon.kraken.dto.Ohlc;
import com.echsylon.kraken.internal.CallCounter;
import com.google.gson.reflect.TypeToken;

import static com.echsylon.kraken.internal.Utils.asString;

/**
 * This class can build a request that retrieves information about the
 * Open/High/Low/Close state for a tradable asset pair.
 * <p>
 * For further technical details see Kraken API documentation at:
 * https://www.kraken.com/help/api
 */
@SuppressWarnings("WeakerAccess")
public class OhlcDataRequestBuilder extends RequestBuilder<Dictionary<Ohlc[]>> {

    /**
     * Creates a new request builder.
     *
     * @param callCounter The request call counter. May be null.
     * @param baseUrl     The base url of the request.
     * @param key         The user API key.
     * @param secret      The corresponding secret.
     */
    public OhlcDataRequestBuilder(final CallCounter callCounter,
                                  final String baseUrl,
                                  final String key,
                                  final byte[] secret) {

        super(1, callCounter, key, secret, baseUrl,
                "GET", "/0/public/OHLC",
                TypeToken.getParameterized(
                        Dictionary.class,
                        Ohlc[].class).getType());
    }

    /**
     * Sets the asset pair request property.
     *
     * @param pair The asset pair to get OHLC data for.
     * @return This request builder instance allowing method call chaining.
     */
    public OhlcDataRequestBuilder useAssetPair(String pair) {
        data.put("pair", pair);
        return this;
    }

    /**
     * Sets the interval request property.
     *
     * @param interval The time span base (in minutes) for the OHLC data.
     *                 Options: 1 (default), 5, 15, 30, 60, 240, 1440, 10080,
     *                 21600
     * @return This request builder instance allowing method call chaining.
     */
    public OhlcDataRequestBuilder useInterval(int interval) {
        data.put("interval", asString(interval));
        return this;
    }

    /**
     * Sets the since id request property.
     *
     * @param sinceId The exclusive epoch describing how far back in time to get
     *                OHLC data from.
     * @return This request builder instance allowing method call chaining.
     */
    public OhlcDataRequestBuilder useSinceId(String sinceId) {
        data.put("since", sinceId);
        return this;
    }

}
