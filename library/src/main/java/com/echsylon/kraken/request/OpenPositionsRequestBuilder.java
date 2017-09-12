package com.echsylon.kraken.request;

import com.echsylon.kraken.Dictionary;
import com.echsylon.kraken.dto.Position;
import com.echsylon.kraken.internal.CallCounter;
import com.google.gson.reflect.TypeToken;

import static com.echsylon.kraken.internal.Utils.asString;
import static com.echsylon.kraken.internal.Utils.join;

/**
 * This class can build a request that retrieves information about any open
 * positions.
 * <p>
 * For further technical details see Kraken API documentation at:
 * https://www.kraken.com/help/api
 */
@SuppressWarnings("WeakerAccess")
public class OpenPositionsRequestBuilder extends RequestBuilder<Dictionary<Position>> {

    /**
     * Creates a new request builder.
     *
     * @param callCounter The request call counter. May be null.
     * @param baseUrl     The base url of the request.
     * @param key         The user API key.
     * @param secret      The corresponding secret.
     */
    public OpenPositionsRequestBuilder(final CallCounter callCounter,
                                       final String baseUrl,
                                       final String key,
                                       final byte[] secret) {

        super(1, callCounter, key, secret, baseUrl,
                "POST", "/0/private/OpenPositions",
                TypeToken.getParameterized(
                        Dictionary.class,
                        Position.class).getType());
    }


    /**
     * Sets the one time password to use when performing the request.
     *
     * @param oneTimePassword The password.
     * @return This request builder instance allowing method call chaining.
     */
    public OpenPositionsRequestBuilder useOneTimePassword(final String oneTimePassword) {
        data.put("otp", oneTimePassword);
        return this;
    }

    /**
     * Sets the do calculations request property.
     *
     * @param performCalculations Whether to include profit/loss calculations.
     *                            Defaults to false.
     * @return This request builder instance allowing method call chaining.
     */
    public OpenPositionsRequestBuilder useCalculationsFlag(boolean performCalculations) {
        data.put("docalcs", asString(performCalculations));
        return this;
    }

    /**
     * Sets the transaction ids request property.
     *
     * @param transactionIds Transaction ids to restrict the result to.
     * @return This request builder instance allowing method call chaining.
     */
    public OpenPositionsRequestBuilder useTransactions(String... transactionIds) {
        data.put("txid", join(transactionIds));
        return this;
    }

}
