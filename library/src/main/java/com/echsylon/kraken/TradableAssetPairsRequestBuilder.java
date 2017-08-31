package com.echsylon.kraken;

import com.echsylon.kraken.dto.AssetPair;
import com.google.gson.reflect.TypeToken;

import static com.echsylon.kraken.Utils.join;

/**
 * This class can build a request that retrieves information about tradable
 * asset pairs. See API documentation on supported asset pair formats.
 * <p>
 * For further technical details see Kraken API documentation at:
 * https://www.kraken.com/help/api
 */
@SuppressWarnings("WeakerAccess")
public class TradableAssetPairsRequestBuilder extends RequestBuilder<Dictionary<AssetPair>> {

    /**
     * Constructor, intentionally hidden from public use.
     *
     * @param callCounter The request call counter. May be null.
     * @param baseUrl     The base url of the request.
     * @param key         The user API key.
     * @param secret      The corresponding secret.
     */
    TradableAssetPairsRequestBuilder(final CallCounter callCounter,
                                            final String baseUrl,
                                            final String key,
                                            final byte[] secret) {

        super(1, callCounter, key, secret, baseUrl,
                "GET", "/0/public/AssetPairs",
                TypeToken.getParameterized(
                        Dictionary.class,
                        AssetPair.class).getType());
    }

    /**
     * Sets the info level request property.
     *
     * @param info The level of information to get. Options: "info" (all,
     *             default), "leverage", "fees", "margin"
     * @return This request builder instance allowing method call chaining.
     */
    public TradableAssetPairsRequestBuilder useInfo(String info) {
        data.put("info", info);
        return this;
    }

    /**
     * Sets the info level request property.
     *
     * @param pairs The asset pairs to get information on. Defaults to all.
     * @return This request builder instance allowing method call chaining.
     */
    public TradableAssetPairsRequestBuilder useAssetPairs(String... pairs) {
        data.put("pair", join(pairs));
        return this;
    }

}
