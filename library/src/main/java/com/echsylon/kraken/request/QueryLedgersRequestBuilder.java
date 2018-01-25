package com.echsylon.kraken.request;

import com.echsylon.kraken.Dictionary;
import com.echsylon.kraken.dto.Ledger;
import com.echsylon.kraken.internal.CallCounter;
import com.google.gson.reflect.TypeToken;

import static com.echsylon.kraken.internal.Utils.join;

/**
 * This class can build a request that retrieves information about any
 * particular ledger(s).
 * <p>
 * For further technical details see Kraken API documentation at:
 * https://www.kraken.com/help/api
 */
@SuppressWarnings("WeakerAccess")
public class QueryLedgersRequestBuilder extends RequestBuilder<Dictionary<Ledger>, QueryLedgersRequestBuilder> {

    /**
     * Creates a new request builder.
     *
     * @param callCounter The request call counter. May be null.
     * @param baseUrl     The base url of the request.
     * @param key         The user API key.
     * @param secret      The corresponding secret.
     */
    public QueryLedgersRequestBuilder(final CallCounter callCounter,
                                      final String baseUrl,
                                      final String key,
                                      final byte[] secret) {

        super(2, callCounter, key, secret, baseUrl,
                "POST", "/0/private/QueryLedgers",
                TypeToken.getParameterized(
                        Dictionary.class,
                        Ledger.class).getType());
    }


    /**
     * Sets the one time password to use when performing the request.
     *
     * @param oneTimePassword The password.
     * @return This request builder instance allowing method call chaining.
     */
    public QueryLedgersRequestBuilder useOneTimePassword(final String oneTimePassword) {
        data.put("otp", oneTimePassword);
        return this;
    }

    /**
     * Sets the ledger ids request property.
     *
     * @param ledgerIds Id of ledgers to get. 20 max, at least one is required.
     * @return This request builder instance allowing method call chaining.
     */
    public QueryLedgersRequestBuilder useLedgers(final String... ledgerIds) {
        data.put("id", join(ledgerIds));
        return this;
    }

}
