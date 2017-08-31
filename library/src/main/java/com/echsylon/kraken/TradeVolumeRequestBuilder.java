package com.echsylon.kraken;

import com.echsylon.kraken.dto.TradeVolume;

import static com.echsylon.kraken.Utils.asString;
import static com.echsylon.kraken.Utils.join;

/**
 * This class can build a request that retrieves information about the current
 * trade volumes.
 * <p>
 * For further technical details see Kraken API documentation at:
 * https://www.kraken.com/help/api
 */
@SuppressWarnings("WeakerAccess")
public class TradeVolumeRequestBuilder extends RequestBuilder<TradeVolume> {

    /**
     * Constructor, intentionally hidden from public use.
     *
     * @param callCounter The request call counter. May be null.
     * @param baseUrl     The base url of the request.
     * @param key         The user API key.
     * @param secret      The corresponding secret.
     */
    TradeVolumeRequestBuilder(final CallCounter callCounter,
                              final String baseUrl,
                              final String key,
                              final byte[] secret) {

        super(1, callCounter, key, secret, baseUrl,
                "POST", "/0/private/TradeVolume",
                TradeVolume.class);
    }


    /**
     * Sets the fee info request property.
     *
     * @param includeFeeInfo Whether to include fee info. Defaults to false.
     * @return This request builder instance allowing method call chaining.
     */
    public TradeVolumeRequestBuilder useFeeInfoFlag(boolean includeFeeInfo) {
        data.put("fee-info", asString(includeFeeInfo));
        return this;
    }

    /**
     * Sets the fee info request property.
     *
     * @param pairs The fee info asset pairs.
     * @return This request builder instance allowing method call chaining.
     */
    public TradeVolumeRequestBuilder useAssetPairs(String... pairs) {
        data.put("pair", join(pairs));
        return this;
    }

}
