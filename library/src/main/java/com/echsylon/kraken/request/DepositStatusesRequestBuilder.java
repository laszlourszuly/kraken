package com.echsylon.kraken.request;

import com.echsylon.kraken.dto.DepositStatus;
import com.echsylon.kraken.internal.CallCounter;

/**
 * This class can build a request that retrieves information about recent
 * deposit statuses for an asset. See API documentation on supported asset
 * formats.
 * <p>
 * For further technical details see Kraken API documentation at:
 * https://www.kraken.com/help/api
 */
@SuppressWarnings("WeakerAccess")
public class DepositStatusesRequestBuilder extends RequestBuilder<DepositStatus[], DepositStatusesRequestBuilder> {

    /**
     * Creates a new request builder.
     *
     * @param callCounter The request call counter. May be null.
     * @param baseUrl     The base url of the request.
     * @param key         The user API key.
     * @param secret      The corresponding secret.
     */
    public DepositStatusesRequestBuilder(final CallCounter callCounter,
                                         final String baseUrl,
                                         final String key,
                                         final byte[] secret) {

        super(1, callCounter, key, secret, baseUrl,
                "POST", "/0/private/DepositStatus",
                DepositStatus[].class);
    }


    /**
     * Sets the one time password to use when performing the request.
     *
     * @param oneTimePassword The password.
     * @return This request builder instance allowing method call chaining.
     */
    public DepositStatusesRequestBuilder useOneTimePassword(final String oneTimePassword) {
        data.put("otp", oneTimePassword);
        return this;
    }

    /**
     * Sets the asset class request property.
     *
     * @param assetClass The type of the asset to fetch info on.
     * @return This request builder instance allowing method call chaining.
     */
    public DepositStatusesRequestBuilder useAssetClass(final String assetClass) {
        data.put("aclass", assetClass);
        return this;
    }

    /**
     * Sets the assets request property.
     *
     * @param asset The asset to get info on.
     * @return This request builder instance allowing method call chaining.
     */
    public DepositStatusesRequestBuilder useAsset(final String asset) {
        data.put("asset", asset);
        return this;
    }

    /**
     * Sets the method request property.
     *
     * @param method The deposit method name.
     * @return This request builder instance allowing method call chaining.
     */
    public DepositStatusesRequestBuilder useMethod(final String method) {
        data.put("method", method);
        return this;
    }

}
