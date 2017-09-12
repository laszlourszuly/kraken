package com.echsylon.kraken.request;

import com.echsylon.kraken.dto.TradeVolume;
import com.echsylon.kraken.internal.CallCounter;

import static com.echsylon.kraken.internal.Utils.asString;
import static com.echsylon.kraken.internal.Utils.join;

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
     * Creates a new request builder.
     *
     * @param callCounter The request call counter. May be null.
     * @param baseUrl     The base url of the request.
     * @param key         The user API key.
     * @param secret      The corresponding secret.
     */
    public TradeVolumeRequestBuilder(final CallCounter callCounter,
                                     final String baseUrl,
                                     final String key,
                                     final byte[] secret) {

        super(1, callCounter, key, secret, baseUrl,
                "POST", "/0/private/TradeVolume",
                TradeVolume.class);
    }


    /**
     * Sets the one time password to use when performing the request.
     *
     * @param oneTimePassword The password.
     * @return This request builder instance allowing method call chaining.
     */
    public TradeVolumeRequestBuilder useOneTimePassword(final String oneTimePassword) {
        data.put("otp", oneTimePassword);
        return this;
    }

    /**
     * Sets the fee info request property.
     *
     * @param includeFeeInfo Whether to include fee info. Defaults to false.
     * @return This request builder instance allowing method call chaining.
     */
    public TradeVolumeRequestBuilder useFeeInfoFlag(final boolean includeFeeInfo) {
        data.put("fee-info", asString(includeFeeInfo));
        return this;
    }

    /**
     * Sets the fee info request property.
     *
     * @param pairs The fee info asset pairs.
     * @return This request builder instance allowing method call chaining.
     */
    public TradeVolumeRequestBuilder useAssetPairs(final String... pairs) {
        data.put("pair", join(pairs));
        return this;
    }

}
