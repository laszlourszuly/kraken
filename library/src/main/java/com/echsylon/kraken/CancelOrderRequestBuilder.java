package com.echsylon.kraken;

import com.echsylon.kraken.dto.OrderCancelReceipt;

/**
 * This class can build a request that cancels an open order.
 * <p>
 * For further technical details see Kraken API documentation at:
 * https://www.kraken.com/help/api
 */
@SuppressWarnings("WeakerAccess")
public class CancelOrderRequestBuilder extends RequestBuilder<OrderCancelReceipt> {

    /**
     * Constructor, intentionally hidden from public use.
     *
     * @param callCounter The request call counter. May be null.
     * @param baseUrl     The base url of the request.
     * @param key         The user API key.
     * @param secret      The corresponding secret.
     */
    CancelOrderRequestBuilder(final CallCounter callCounter,
                              final String baseUrl,
                              final String key,
                              final byte[] secret) {

        super(0, callCounter, key, secret, baseUrl,
                "POST", "/0/private/CancelOrder",
                OrderCancelReceipt.class);
    }


    /**
     * Sets the one time password to use when performing the request.
     *
     * @param oneTimePassword The password.
     * @return This request builder instance allowing method call chaining.
     */
    public CancelOrderRequestBuilder useOneTimePassword(final String oneTimePassword) {
        data.put("otp", oneTimePassword);
        return this;
    }

    /**
     * Sets the order id request property.
     *
     * @param id The transaction or user reference id of the order(s) to close.
     * @return This request builder instance allowing method call chaining.
     */
    public CancelOrderRequestBuilder useOrderId(String id) {
        data.put("txid", id);
        return this;
    }

}
