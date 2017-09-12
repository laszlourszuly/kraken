package com.echsylon.kraken.request;

import com.echsylon.kraken.Dictionary;
import com.echsylon.kraken.dto.Order;
import com.echsylon.kraken.internal.CallCounter;
import com.google.gson.reflect.TypeToken;

import static com.echsylon.kraken.internal.Utils.asString;

/**
 * This class can build a request that retrieves information about any closed
 * orders.
 * <p>
 * For further technical details see Kraken API documentation at:
 * https://www.kraken.com/help/api
 */
@SuppressWarnings("WeakerAccess")
public class ClosedOrdersRequestBuilder extends RequestBuilder<Dictionary<Order>> {

    /**
     * Creates a new request builder.
     *
     * @param callCounter The request call counter. May be null.
     * @param baseUrl     The base url of the request.
     * @param key         The user API key.
     * @param secret      The corresponding secret.
     */
    public ClosedOrdersRequestBuilder(final CallCounter callCounter,
                                      final String baseUrl,
                                      final String key,
                                      final byte[] secret) {

        super(1, callCounter, key, secret, baseUrl,
                "POST", "/0/private/ClosedOrders",
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
    public ClosedOrdersRequestBuilder useOneTimePassword(final String oneTimePassword) {
        data.put("otp", oneTimePassword);
        return this;
    }

    /**
     * Sets the include trades request property.
     *
     * @param includeTrades Whether to include trades. Defaults to false.
     * @return This request builder instance allowing method call chaining.
     */
    public ClosedOrdersRequestBuilder useTradesFlag(boolean includeTrades) {
        data.put("trades", asString(includeTrades));
        return this;
    }

    /**
     * Sets the user reference request property.
     *
     * @param userReferenceId Restrict results to given user reference id.
     * @return This request builder instance allowing method call chaining.
     */
    public ClosedOrdersRequestBuilder useReference(String userReferenceId) {
        data.put("userref", userReferenceId);
        return this;
    }

    /**
     * Sets the start request property.
     *
     * @param start Start time or transaction id of results. Exclusive.
     * @return This request builder instance allowing method call chaining.
     */
    public ClosedOrdersRequestBuilder useStartTag(String start) {
        data.put("start", start);
        return this;
    }

    /**
     * Sets the end request property.
     *
     * @param end End time or transaction id of results. Inclusive.
     * @return This request builder instance allowing method call chaining.
     */
    public ClosedOrdersRequestBuilder useEndTag(String end) {
        data.put("end", end);
        return this;
    }

    /**
     * Sets the offset request property.
     *
     * @param offset Result offset.
     * @return This request builder instance allowing method call chaining.
     */
    public ClosedOrdersRequestBuilder useOffset(int offset) {
        data.put("ofs", asString(offset));
        return this;
    }

    /**
     * Sets the close time request property.
     *
     * @param closeTime Which time to use. Options: "both" (default), "open",
     *                  "close".
     * @return This request builder instance allowing method call chaining.
     */
    public ClosedOrdersRequestBuilder useCloseTime(String closeTime) {
        data.put("closetime", closeTime);
        return this;
    }

}
