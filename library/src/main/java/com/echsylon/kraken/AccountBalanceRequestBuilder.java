package com.echsylon.kraken;

import com.google.gson.reflect.TypeToken;

/**
 * This class can build a request that retrieves information on the account
 * balance for all associated assets.
 * <p>
 * For further technical details see Kraken API documentation at:
 * https://www.kraken.com/help/api
 */
@SuppressWarnings("WeakerAccess")
public class AccountBalanceRequestBuilder extends RequestBuilder<Dictionary<String>> {

    /**
     * Constructor, intentionally hidden from public use.
     *
     * @param callCounter The request call counter. May be null.
     * @param baseUrl     The base url of the request.
     * @param key         The user API key.
     * @param secret      The corresponding secret.
     */
    AccountBalanceRequestBuilder(final CallCounter callCounter,
                                 final String baseUrl,
                                 final String key,
                                 final byte[] secret) {

        super(1, callCounter, key, secret, baseUrl,
                "POST", "/0/private/Balance",
                TypeToken.getParameterized(
                        Dictionary.class,
                        String.class).getType());
    }

}
