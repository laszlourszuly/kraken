package com.echsylon.kraken;

import com.echsylon.kraken.dto.Asset;
import com.google.gson.reflect.TypeToken;

import static com.echsylon.kraken.Utils.join;

/**
 * This class can build a request that retrieves information about supported
 * currency assets. See API documentation on supported asset formats.
 * <p>
 * For further technical details see Kraken API documentation at:
 * https://www.kraken.com/help/api
 */
@SuppressWarnings("WeakerAccess")
public class AssetInfoRequestBuilder extends RequestBuilder<Dictionary<Asset>> {

    /**
     * Constructor, intentionally hidden from public use.
     *
     * @param callCounter The request call counter. May be null.
     * @param baseUrl     The base url of the request.
     * @param key         The user API key.
     * @param secret      The corresponding secret.
     */
    AssetInfoRequestBuilder(final CallCounter callCounter,
                                   final String baseUrl,
                                   final String key,
                                   final byte[] secret) {

        super(1, callCounter, key, secret, baseUrl,
                "GET", "/0/public/Assets",
                TypeToken.getParameterized(
                        Dictionary.class,
                        Asset.class).getType());
    }

    /**
     * Sets the info request property.
     *
     * @param info The level of information to get.
     * @return This request builder instance allowing method call chaining.
     */
    public AssetInfoRequestBuilder useInfo(String info) {
        data.put("info", info);
        return this;
    }

    /**
     * Sets the asset class request property.
     *
     * @param assetClass The type of the asset to fetch info on.
     * @return This request builder instance allowing method call chaining.
     */
    public AssetInfoRequestBuilder useAssetClass(String assetClass) {
        data.put("aclass", assetClass);
        return this;
    }

    /**
     * Sets the assets request property.
     *
     * @param assets The assets to get info on.
     * @return This request builder instance allowing method call chaining.
     */
    public AssetInfoRequestBuilder useAssets(String... assets) {
        data.put("assets", join(assets));
        return this;
    }

}
