package com.echsylon.kraken.request;

import com.echsylon.kraken.Dictionary;
import com.echsylon.kraken.dto.TradeHistory;
import com.echsylon.kraken.internal.CallCounter;
import com.google.gson.reflect.TypeToken;

import static com.echsylon.kraken.internal.Utils.asString;
import static com.echsylon.kraken.internal.Utils.join;

/**
 * This class can build a request that retrieves information about any
 * particular trade(s).
 * <p>
 * For further technical details see Kraken API documentation at:
 * https://www.kraken.com/help/api
 */
@SuppressWarnings("WeakerAccess")
public class QueryTradesRequestBuilder extends RequestBuilder<Dictionary<TradeHistory>> {

    /**
     * Creates a new request builder.
     *
     * @param callCounter The request call counter. May be null.
     * @param baseUrl     The base url of the request.
     * @param key         The user API key.
     * @param secret      The corresponding secret.
     */
    public QueryTradesRequestBuilder(final CallCounter callCounter,
                                     final String baseUrl,
                                     final String key,
                                     final byte[] secret) {

        super(2, callCounter, key, secret, baseUrl,
                "POST", "/0/private/QueryTrades",
                TypeToken.getParameterized(
                        Dictionary.class,
                        TradeHistory.class).getType());
    }


    /**
     * Sets the one time password to use when performing the request.
     *
     * @param oneTimePassword The password.
     * @return This request builder instance allowing method call chaining.
     */
    public QueryTradesRequestBuilder useOneTimePassword(final String oneTimePassword) {
        data.put("otp", oneTimePassword);
        return this;
    }

    /**
     * Sets the include trades request property.
     *
     * @param includePositionTrades Whether to include trades related to
     *                              position. Defaults to false.
     * @return This request builder instance allowing method call chaining.
     */
    public QueryTradesRequestBuilder useTradesFlag(boolean includePositionTrades) {
        data.put("trades", asString(includePositionTrades));
        return this;
    }

    /**
     * Sets the transaction ids request property.
     *
     * @param transactionIds Transaction ids of the orders to get. 20 max, at
     *                       least one is required.
     * @return This request builder instance allowing method call chaining.
     */
    public QueryTradesRequestBuilder useTransactions(String... transactionIds) {
        data.put("txid", join(transactionIds));
        return this;
    }

}
