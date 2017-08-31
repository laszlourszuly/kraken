package com.echsylon.kraken;

import com.echsylon.kraken.dto.Order;
import com.google.gson.reflect.TypeToken;

import static com.echsylon.kraken.Utils.asString;

/**
 * This class can build a request that retrieves information about any open
 * orders.
 * <p>
 * For further technical details see Kraken API documentation at:
 * https://www.kraken.com/help/api
 */
@SuppressWarnings("WeakerAccess")
public class OpenOrdersRequestBuilder extends RequestBuilder<Dictionary<Order>> {

    /**
     * Constructor, intentionally hidden from public use.
     *
     * @param callCounter The request call counter. May be null.
     * @param baseUrl     The base url of the request.
     * @param key         The user API key.
     * @param secret      The corresponding secret.
     */
    OpenOrdersRequestBuilder(final CallCounter callCounter,
                                    final String baseUrl,
                                    final String key,
                                    final byte[] secret) {

        super(1, callCounter, key, secret, baseUrl,
                "POST", "/0/private/OpenOrders",
                TypeToken.getParameterized(
                        Dictionary.class,
                        Order.class).getType());
    }


    /**
     * Sets the one time password to use when performing the request.
     *
     * @param oneTimePassword The password.
     * @return This request builder instance allowing method call chaining.
     */
    public OpenOrdersRequestBuilder useOneTimePassword(final String oneTimePassword) {
        data.put("otp", oneTimePassword);
        return this;
    }

    /**
     * Sets the include trades request property.
     *
     * @param includeTrades Whether to include trades. Defaults to false.
     * @return This request builder instance allowing method call chaining.
     */
    public OpenOrdersRequestBuilder useTradesFlag(boolean includeTrades) {
        data.put("trades", asString(includeTrades));
        return this;
    }

    /**
     * Sets the user reference request property.
     *
     * @param userReferenceId Restrict results to given user reference id.
     * @return This request builder instance allowing method call chaining.
     */
    public OpenOrdersRequestBuilder useReference(String userReferenceId) {
        data.put("userref", userReferenceId);
        return this;
    }

}
