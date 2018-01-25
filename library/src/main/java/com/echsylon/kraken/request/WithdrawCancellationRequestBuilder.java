package com.echsylon.kraken.request;

import com.echsylon.kraken.internal.CallCounter;

/**
 * This class can build a request that tries to cancel a previously placed
 * withdrawal of funds.
 * <p>
 * For further technical details see Kraken API documentation at:
 * https://www.kraken.com/help/api
 */
@SuppressWarnings("WeakerAccess")
public class WithdrawCancellationRequestBuilder extends RequestBuilder<Boolean, WithdrawCancellationRequestBuilder> {

    /**
     * Creates a new request builder.
     *
     * @param callCounter The request call counter. May be null.
     * @param baseUrl     The base url of the request.
     * @param key         The user API key.
     * @param secret      The corresponding secret.
     */
    public WithdrawCancellationRequestBuilder(final CallCounter callCounter,
                                              final String baseUrl,
                                              final String key,
                                              final byte[] secret) {

        super(1, callCounter, key, secret, baseUrl,
                "POST", "/0/private/WithdrawCancel",
                Boolean.class);
    }


    /**
     * Sets the one time password to use when performing the request.
     *
     * @param oneTimePassword The password.
     * @return This request builder instance allowing method call chaining.
     */
    public WithdrawCancellationRequestBuilder useOneTimePassword(final String oneTimePassword) {
        data.put("otp", oneTimePassword);
        return this;
    }

    /**
     * Sets the reference id request property.
     *
     * @param referenceId The reference id of the withdrawal to cancel.
     * @return This request builder instance allowing method call chaining.
     */
    public WithdrawCancellationRequestBuilder useReferenceId(final String referenceId) {
        data.put("refid", referenceId);
        return this;
    }

}
