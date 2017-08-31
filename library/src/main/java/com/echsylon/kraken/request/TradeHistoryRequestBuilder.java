package com.echsylon.kraken.request;

import com.echsylon.kraken.Dictionary;
import com.echsylon.kraken.dto.TradeHistory;
import com.echsylon.kraken.internal.CallCounter;
import com.google.gson.reflect.TypeToken;

import static com.echsylon.kraken.internal.Utils.asString;

/**
 * This class can build a request that retrieves trades history.
 * <p>
 * For further technical details see Kraken API documentation at:
 * https://www.kraken.com/help/api
 */
@SuppressWarnings("WeakerAccess")
public class TradeHistoryRequestBuilder extends RequestBuilder<Dictionary<TradeHistory>> {

    /**
     * Constructor, intentionally hidden from public use.
     *
     * @param callCounter The request call counter. May be null.
     * @param baseUrl     The base url of the request.
     * @param key         The user API key.
     * @param secret      The corresponding secret.
     */
    public TradeHistoryRequestBuilder(final CallCounter callCounter,
                                      final String baseUrl,
                                      final String key,
                                      final byte[] secret) {

        super(2, callCounter, key, secret, baseUrl,
                "POST", "/0/private/TradesHistory",
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
    public TradeHistoryRequestBuilder useOneTimePassword(final String oneTimePassword) {
        data.put("otp", oneTimePassword);
        return this;
    }

    /**
     * Sets the trade type request property.
     *
     * @param type The type of trades to get. Options: "all" (default), "any
     *             position", "closed position", "closing position", "no
     *             position".
     * @return This request builder instance allowing method call chaining.
     */
    public TradeHistoryRequestBuilder useType(String type) {
        data.put("type", type);
        return this;
    }

    /**
     * Sets the include trades request property.
     *
     * @param includeTrades Whether to include trades. Defaults to false.
     * @return This request builder instance allowing method call chaining.
     */
    public TradeHistoryRequestBuilder useTradesFlag(boolean includeTrades) {
        data.put("trades", asString(includeTrades));
        return this;
    }

    /**
     * Sets the start request property.
     *
     * @param start Start time or transaction id of results. Exclusive.
     * @return This request builder instance allowing method call chaining.
     */
    public TradeHistoryRequestBuilder useStartTag(String start) {
        data.put("start", start);
        return this;
    }

    /**
     * Sets the end request property.
     *
     * @param end End time or transaction id of results. Inclusive.
     * @return This request builder instance allowing method call chaining.
     */
    public TradeHistoryRequestBuilder useEndTag(String end) {
        data.put("end", end);
        return this;
    }

    /**
     * Sets the offset request property.
     *
     * @param offset Result offset.
     * @return This request builder instance allowing method call chaining.
     */
    public TradeHistoryRequestBuilder useOffset(int offset) {
        data.put("ofs", asString(offset));
        return this;
    }

}
