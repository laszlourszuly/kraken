package com.echsylon.kraken;

import com.echsylon.kraken.dto.TradeBalance;

/**
 * This class can build a request that retrieves the trade balance of an asset.
 * <p>
 * For further technical details see Kraken API documentation at:
 * https://www.kraken.com/help/api
 */
@SuppressWarnings("WeakerAccess")
public class TradeBalanceRequestBuilder extends RequestBuilder<TradeBalance> {

    /**
     * Constructor, intentionally hidden from public use.
     *
     * @param callCounter The request call counter. May be null.
     * @param baseUrl     The base url of the request.
     * @param key         The user API key.
     * @param secret      The corresponding secret.
     */
    TradeBalanceRequestBuilder(final CallCounter callCounter,
                                      final String baseUrl,
                                      final String key,
                                      final byte[] secret) {

        super(1, callCounter, key, secret, baseUrl,
                "POST", "/0/private/TradeBalance",
                TradeBalance.class);
    }


    /**
     * Sets the one time password to use when performing the request.
     *
     * @param oneTimePassword The password.
     * @return This request builder instance allowing method call chaining.
     */
    public TradeBalanceRequestBuilder useOneTimePassword(final String oneTimePassword) {
        data.put("otp", oneTimePassword);
        return this;
    }

    /**
     * Sets the base asset class request property.
     *
     * @param assetClass The type of the base asset. Defaults to "currency".
     * @return This request builder instance allowing method call chaining.
     */
    public TradeBalanceRequestBuilder useAssetClass(String assetClass) {
        data.put("aclass", assetClass);
        return this;
    }

    /**
     * Sets the base asset request property.
     *
     * @param baseAssets The base asset to use when determining the balance.
     *                   Defaults to "ZUSD".
     * @return This request builder instance allowing method call chaining.
     */
    public TradeBalanceRequestBuilder useBaseAsset(String baseAssets) {
        data.put("asset", baseAssets);
        return this;
    }

}
