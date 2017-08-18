package com.echsylon.kraken;

import android.net.Uri;

import com.echsylon.blocks.callback.DefaultRequest;
import com.echsylon.blocks.callback.Request;
import com.echsylon.blocks.network.GsonJsonParser;
import com.echsylon.blocks.network.NetworkClient;
import com.echsylon.blocks.network.OkHttpNetworkClient;
import com.echsylon.kraken.dto.Asset;
import com.echsylon.kraken.dto.AssetPair;
import com.echsylon.kraken.dto.Depth;
import com.echsylon.kraken.dto.Ledger;
import com.echsylon.kraken.dto.Ohlc;
import com.echsylon.kraken.dto.Order;
import com.echsylon.kraken.dto.OrderCancelState;
import com.echsylon.kraken.dto.OrderReceipt;
import com.echsylon.kraken.dto.Position;
import com.echsylon.kraken.dto.Spread;
import com.echsylon.kraken.dto.Ticker;
import com.echsylon.kraken.dto.Time;
import com.echsylon.kraken.dto.Trade;
import com.echsylon.kraken.dto.TradeBalance;
import com.echsylon.kraken.dto.TradeHistory;
import com.echsylon.kraken.dto.TradeVolume;
import com.echsylon.kraken.exception.KrakenRequestException;
import com.google.gson.TypeAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.echsylon.kraken.Utils.asBytes;
import static com.echsylon.kraken.Utils.asString;
import static com.echsylon.kraken.Utils.base64Decode;
import static com.echsylon.kraken.Utils.base64Encode;
import static com.echsylon.kraken.Utils.concat;
import static com.echsylon.kraken.Utils.hmacSha512;
import static com.echsylon.kraken.Utils.join;
import static com.echsylon.kraken.Utils.sha256;

/**
 * This class describes the Kraken API.
 * <p>
 * For further technical details see Kraken API documentation at:
 * https://www.kraken.com/help/api
 */
public class Kraken {
    private static final String BASE_URL = "https://api.kraken.com";

    /**
     * Initiates the Kraken API Client local cache metrics.
     *
     * @param cacheDirectory The location on disk to save the cache in.
     * @param cacheSizeBytes The max number of bytes to allocate to caching.
     */
    public static void setup(final File cacheDirectory, final int cacheSizeBytes) {
        OkHttpNetworkClient.settings(
                new OkHttpNetworkClient.Settings(
                        cacheDirectory,
                        cacheSizeBytes,
                        false,          // Follow http -> http redirects
                        false));        // Follow https -> http redirects
    }


    private String baseUrl;
    private String key;
    private byte[] secret;

    /**
     * Allows test cases to redirect requests to a test environment. Protected
     * to keep false assumptions away.
     *
     * @param baseUrl The base url to the test environment.
     */
    Kraken(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * Initializes an instance of the Kraken API Client that's only capable of
     * accessing the public Kraken API.
     */
    public Kraken() {
        this(null, null);
    }

    /**
     * Initializes an instance of the Kraken API Client that's capable of
     * executing private API requests (given that the provided credentials are
     * valid).
     *
     * @param apiKey    The Kraken API key for the targeted account.
     * @param apiSecret The corresponding key secret.
     */
    public Kraken(String apiKey, String apiSecret) {
        baseUrl = BASE_URL;
        key = apiKey;
        secret = base64Decode(apiSecret);
    }

    /**
     * Retrieves the current server time. This is to aid in approximating the
     * skew time between the server and client.
     *
     * @return A request builder object to configure any client side cache
     * metrics with and to attach any callback implementations to.
     */
    public KrakenRequestBuilder<Time> getServerTime() {
        return getRequestBuilder("/0/public/Time", "get");
    }

    /**
     * Retrieves information about supported currency assets. See API
     * documentation on supported asset formats.
     *
     * @param info       The level of information to get.
     * @param assetClass The type of the asset.
     * @param assets     The assets to get info on.
     * @return A request builder object to configure any client side cache
     * metrics with and to attach any callback implementations to.
     */
    public KrakenRequestBuilder<List<Asset>> getAssetInfo(final String info,
                                                          final String assetClass,
                                                          final String... assets) {
        return getRequestBuilder(
                new AssetTypeAdapter(),
                "/0/public/Assets",
                "get",
                "info", info,
                "aclass", assetClass,
                "assets", join(assets));
    }

    /**
     * Retrieves information about tradable asset pairs. Not providing any
     * specific asset pairs will return info for all known assets.
     * <p>
     * See API documentation on supported asset pair formats.
     *
     * @param info  The level of information to get. Options: "info" (all,
     *              default), "leverage", "fees", "margin"
     * @param pairs The currencies to fetch information for.
     * @return A request builder object to configure any client side cache
     * metrics with and to attach any callback implementations to.
     */
    public KrakenRequestBuilder<AssetPair> getTradableAssetPairs(final String info,
                                                                 final String... pairs) {
        return getRequestBuilder(
                "/0/public/AssetPairs",
                "get",
                "info", info,
                "pair", join(pairs));
    }

    /**
     * Retrieves information about the ticker state for tradable asset pairs.
     *
     * @param pairs The asset pairs to fetch information on. At least one must
     *              be given or the server will fail.
     * @return A request builder object to configure any client side cache *
     * metrics with and to attach any callback implementations to.
     */
    public KrakenRequestBuilder<Map<String, Ticker>> getTickerInformation(final String... pairs) {
        return getRequestBuilder(
                new TickerTypeAdapter(),
                "/0/public/Ticker",
                "get",
                "pair", join(pairs));
    }

    /**
     * Retrieves information about the Open/High/Low/Close state for a tradable
     * asset pair.
     *
     * @param pair     The single asset pair to get information for.
     * @param interval The time span base (in minutes) for the OHLC data.
     *                 Options: 1 (default), 5, 15, 30, 60, 240, 1440, 10080,
     *                 21600
     * @param since    The exclusive epoch describing how far back in time to
     *                 get OHLC data from.
     * @return A request builder object to configure any client side cache
     * metrics with and to attach any callback implementations to.
     */
    public KrakenRequestBuilder<KrakenList<Ohlc>> getOhlcData(final String pair,
                                                              final Integer interval,
                                                              final String since) {
        return getRequestBuilder(
                new OhlcTypeAdapter(),
                "/0/public/OHLC",
                "get",
                "pair", pair,
                "interval", asString(interval),
                "since", since);
    }

    /**
     * Retrieves the market depth for tradable asset pairs.
     *
     * @param pair  The asset pair to get market depth for.
     * @param count The maximum number of asks or bids. Optional.
     * @return A request builder object to configure any client side cache
     * metrics with and to attach any callback implementations to.
     */
    public KrakenRequestBuilder<Depth> getOrderBook(final String pair, final Integer count) {
        return getRequestBuilder(
                new DepthTypeAdapter(),
                "/0/public/Depth",
                "get",
                "pair", pair,
                "count", asString(count));
    }

    /**
     * Retrieves recent trades on the market.
     *
     * @param pair  The asset pair to get data for.
     * @param since The trade id of the previous poll. Optional. Exclusive.
     * @return A request builder object to configure any client side cache
     * metrics with and to attach any callback implementations to.
     */
    public KrakenRequestBuilder<KrakenList<Trade>> getRecentTrades(final String pair,
                                                                   final String since) {
        return getRequestBuilder(
                new TradeTypeAdapter(),
                "/0/public/Trades",
                "get",
                "pair", pair,
                "since", since);
    }

    /**
     * Retrieves information about recent spread data for a tradable asset pair.
     *
     * @param pair  The asset pair to get information for.
     * @param since The spread id of the previous poll. Optional. Inclusive.
     * @return A request builder object to configure any client side cache
     * metrics with and to attach any callback implementations to.
     */
    public KrakenRequestBuilder<KrakenList<Spread>> getRecentSpreadData(final String pair,
                                                                        final String since) {
        return getRequestBuilder(
                new SpreadTypeAdapter(),
                "/0/public/Spread",
                "get",
                "pair", pair,
                "since", since);
    }

    // Private user data API

    /**
     * Retrieves the account balance for all associated assets.
     *
     * @return A request builder object to configure any client side cache
     * metrics with and to attach any callback implementations to.
     */
    public KrakenRequestBuilder<Map<String, String>> getAccountBalance() {
        return getRequestBuilder("/0/private/Balance", "post");
    }

    /**
     * Retrieves the trade balance of an asset.
     *
     * @param assetClass The type of asset. Defaults to "currency".
     * @param baseAsset  The base asset to use when determining the balance.
     *                   Defaults to "ZUSD"
     * @return A request builder object to configure any client side cache
     * metrics with and to attach any callback implementations to.
     */
    public KrakenRequestBuilder<TradeBalance> getTradeBalance(final String assetClass,
                                                              final String baseAsset) {
        return getRequestBuilder(
                "/0/private/TradeBalance",
                "post",
                "aclass", assetClass,
                "asset", baseAsset);
    }

    /**
     * Retrieves information about any open orders.
     *
     * @param includeTrades   Whether to include trades. Defaults to false.
     * @param userReferenceId Restrict results to given user reference id.
     * @return A request builder object to configure any client side cache
     * metrics with and to attach any callback implementations to.
     */
    public KrakenRequestBuilder<KrakenList<Order>> getOpenOrders(final Boolean includeTrades,
                                                                 final String userReferenceId) {
        return getRequestBuilder(
                new OrderTypeAdapter(),
                "/0/private/OpenOrders",
                "post",
                "trades", asString(includeTrades),
                "userref", userReferenceId);
    }

    /**
     * Retrieves information about any closed orders.
     *
     * @param includeTrades   Whether to include trades. Defaults to false.
     * @param userReferenceId Restrict results to user reference id. Optional.
     * @param start           Starting time or order transaction id of results.
     *                        Optional. Exclusive.
     * @param end             Ending time or order transaction id of results.
     *                        Optional. Inclusive.
     * @param offset          Result offset.
     * @param closeTime       Which time to use. Options: "open", "close",
     *                        "both" (default).
     * @return A request builder object to configure any client side cache
     * metrics with and to attach any callback implementations to.
     */
    public KrakenRequestBuilder<KrakenList<Order>> getClosedOrders(final Boolean includeTrades,
                                                                   final String userReferenceId,
                                                                   final String start,
                                                                   final String end,
                                                                   final Integer offset,
                                                                   final String closeTime) {
        return getRequestBuilder(
                new OrderTypeAdapter(),
                "/0/private/ClosedOrders",
                "post",
                "trades", asString(includeTrades),
                "userref", userReferenceId,
                "start", start,
                "end", end,
                "ofs", asString(offset),
                "closetime", closeTime);
    }

    /**
     * Retrieves information about any particular order(s)
     *
     * @param includeTrades   Whether to include trades.
     * @param userReferenceId Restrict results to user reference id. Optional.
     * @param transactionId   Transaction ids of the orders to get info about.
     *                        20 max, at least one is required.
     * @return A request builder object to configure any client side cache
     * metrics with and to attach any callback implementations to.
     */
    public KrakenRequestBuilder<KrakenList<Order>> queryOrdersInfo(final Boolean includeTrades,
                                                                   final String userReferenceId,
                                                                   final String... transactionId) {
        return getRequestBuilder(
                new OrderTypeAdapter(),
                "/0/private/QueryOrders",
                "post",
                "trades", asString(includeTrades),
                "userref", userReferenceId,
                "txid", join(transactionId));
    }

    /**
     * Retrieves trades history
     *
     * @param type          The type of trades to get. Options: "all" (default),
     *                      "any position", "closed position", "closing
     *                      position", "no position".
     * @param includeTrades Whether to include trades related to position.
     * @param start         Starting time or trade transaction id of results.
     *                      Optional. Exclusive.
     * @param end           Ending time or trade transaction id of results.
     *                      Optional. Inclusive.
     * @param offset        Result offset.
     * @return A request builder object to configure any client side cache
     * metrics with and to attach any callback implementations to.
     */
    public KrakenRequestBuilder<KrakenList<TradeHistory>> getTradesHistory(final String type,
                                                                           final Boolean includeTrades,
                                                                           final String start,
                                                                           final String end,
                                                                           final Integer offset) {
        return getRequestBuilder(
                new TradeHistoryTypeAdapter(),
                "/0/private/TradesHistory",
                "post",
                "type", type,
                "trades", asString(includeTrades),
                "start", start,
                "end", end,
                "ofs", asString(offset));
    }

    /**
     * Retrieves information about any particular trades(s)
     *
     * @param includePositionTrades Whether to include trades related to
     *                              position. Defaults to false.
     * @param transactionId         Transaction ids of the orders to get info
     *                              about. 20 max, at least one is required.
     * @return A request builder object to configure any client side cache
     * metrics with and to attach any callback implementations to.
     */
    public KrakenRequestBuilder<KrakenList<TradeHistory>> queryTradesInfo(final Boolean includePositionTrades,
                                                                          final String... transactionId) {
        return getRequestBuilder(
                new TradeHistoryTypeAdapter(),
                "/0/private/QueryTrades",
                "post",
                "trades", asString(includePositionTrades),
                "txid", join(transactionId));
    }

    /**
     * Retrieves information about any open positions
     *
     * @param performCalculations Whether to include profit/loss calculations.
     *                            Defaults to false.
     * @param transactionId       Transaction ids to restrict the result to.
     * @return A request builder object to configure any client side cache
     * metrics with and to attach any callback implementations to.
     */
    public KrakenRequestBuilder<KrakenList<Position>> getOpenPositions(final Boolean performCalculations,
                                                                       final String... transactionId) {
        return getRequestBuilder(
                new PositionTypeAdapter(),
                "/0/private/OpenPositions",
                "post",
                "docalcs", asString(performCalculations),
                "txid", join(transactionId));
    }

    /**
     * Retrieves information about the ledgers.
     *
     * @param assetClass The type of asset. Defaults to "currency".
     * @param ledgerType Type of ledger to get. Options: "all" (default),
     *                   "deposit", "withdrawal", "trade", "margin"
     * @param start      Starting time or ledger id of results. Optional.
     *                   Exclusive.
     * @param end        Ending time or ledger id of results. Optional.
     *                   Inclusive.
     * @param offset     Result offset.
     * @param asset      Assets to restrict result to. Defaults to all.
     * @return A request builder object to configure any client side cache
     * metrics with and to attach any callback implementations to.
     */
    public KrakenRequestBuilder<KrakenList<Ledger>> getLedgersInfo(final String assetClass,
                                                                   final String ledgerType,
                                                                   final String start,
                                                                   final String end,
                                                                   final Integer offset,
                                                                   final String... asset) {
        return getRequestBuilder(
                new LedgerTypeAdapter(),
                "/0/private/Ledgers",
                "post",
                "aclass", assetClass,
                "type", ledgerType,
                "start", start,
                "end", end,
                "ofs", asString(offset),
                "asset", join(asset));
    }

    /**
     * Retrieves information about any particular ledger(s).
     *
     * @param ledgerId Id of ledgers to get. 20 max, at least one is required.
     * @return A request builder object to configure any client side cache
     * metrics with and to attach any callback implementations to.
     */
    public KrakenRequestBuilder<KrakenList<Ledger>> queryLedgers(final String... ledgerId) {
        return getRequestBuilder(
                new LedgerTypeAdapter(),
                "/0/private/QueryLedgers",
                "post",
                "id", join(ledgerId));
    }

    /**
     * Retrieves information about the current trade volumes.
     *
     * @param includeFeeInfo   Whether to include fee info. Defaults to false.
     * @param feeInfoAssetPair The fee info asset pairs. Defaults to "ZUSD".
     * @return A request builder object to configure any client side cache
     * metrics with and to attach any callback implementations to.
     */
    public KrakenRequestBuilder<TradeVolume> getTradeVolume(final Boolean includeFeeInfo,
                                                            final String... feeInfoAssetPair) {
        return getRequestBuilder(
                "/0/private/TradeVolume",
                "post",
                "fee-info", asString(includeFeeInfo),
                "pair", join(feeInfoAssetPair));
    }

    // Private user trading

    /**
     * Adds a regular sell or buy order
     *
     * @param pair                Asset pair of the new order.
     * @param type                Nature of the order. Options: "buy", "sell".
     * @param orderType           The type of the order. Options: "market",
     *                            "limit", "stop-loss", "take-profit",
     *                            "stop-loss-profit", "stop-loss-profit-limit",
     *                            "stop-loss-limit", "take-profit-limit",
     *                            "trailing-stop", "trailing-stop-limit",
     *                            "stop-loss-and-limit", "settle-position".
     * @param price               Price (depends on orderType). Optional.
     * @param secondaryPrice      Price2 (depends on orderType). Optional.
     * @param volume              Order volume in lots.
     * @param leverage            Amount of desired leverage. Defaults to none.
     * @param startTime           Scheduled start time as epoch seconds.
     *                            Options: 0 (default), +[n], [n]. Optional.
     * @param expireTime          Expiration time expressed in epoch seconds.
     *                            Options: 0 (no exp. default), +[n], [n].
     *                            Optional.
     * @param userReference       User reference id. 32-bit, signed. Optional.
     * @param closeOrderType      Closing order type. Optional.
     * @param closePrice          Closing order price. Optional.
     * @param closeSecondaryPrice Closing order secondary price. Optional.
     * @param validateOnly        Validate only, do not submit order. Optional.
     * @param flags               Order flags. Options: "viqc", "fcib", "fciq",
     *                            "nompp", "post". Optional.
     * @return A request builder object to configure any client side cache
     * metrics with and to attach any callback implementations to.
     */
    public KrakenRequestBuilder<OrderReceipt> addStandardOrder(final String pair,
                                                               final String type,
                                                               final String orderType,
                                                               final String price,
                                                               final String secondaryPrice,
                                                               final String volume,
                                                               final String leverage,
                                                               final String startTime,
                                                               final String expireTime,
                                                               final String userReference,
                                                               final String closeOrderType,
                                                               final String closePrice,
                                                               final String closeSecondaryPrice,
                                                               final Boolean validateOnly,
                                                               final String... flags) {
        return getRequestBuilder(
                "/0/private/AddOrder",
                "post",
                "pair", pair,
                "type", type,
                "ordertype", orderType,
                "price", price,
                "price2", secondaryPrice,
                "volume", volume,
                "leverage", leverage,
                "oflags", join(flags),
                "starttm", startTime,
                "expiretm", expireTime,
                "userref", userReference,
                "validate", asString(validateOnly),
                "close[ordertype]", closeOrderType,
                "close[price]", closePrice,
                "close[price2]", closeSecondaryPrice);
    }

    /**
     * Cancels an open order.
     *
     * @param id The transaction or user reference id of the order(s) to close.
     * @return A request builder object to configure any client side cache
     * metrics with and to attach any callback implementations to.
     */
    public KrakenRequestBuilder<OrderCancelState> cancelOpenOrder(final String id) {
        return getRequestBuilder("/0/private/CancelOrder", "post", "txid", id);
    }

    // Private boilerplate

    /**
     * Decorates and enqueues a new Kraken request.
     *
     * @param path   The path of the url to request.
     * @param method The HTTP method of the request.
     * @param data   The optional key/value entries to pass along. Even
     *               positions are treated as keys and odd positions as values.
     *               Any trailing keys will be ignored.
     * @param <V>    The type definition of the result Java class.
     * @return A request builder object to configure any client side cache
     * metrics with and to attach any callback implementations to.
     */
    private <V> KrakenRequestBuilder<V> getRequestBuilder(final String path,
                                                          final String method,
                                                          final String... data) {
        return getRequestBuilder(null, path, method, data);
    }

    /**
     * Decorates and enqueues a new Kraken request.
     *
     * @param typeAdapter JSON adapter to parse non-default data structures.
     * @param path        The path of the url to request.
     * @param method      The HTTP method of the request.
     * @param data        The optional key/value entries to pass along. Even
     *                    positions are treated as keys and odd positions as
     *                    values. Any trailing keys will be ignored.
     * @param <V>         The type definition of the result Java class.
     * @return A request builder object to configure any client side cache
     * metrics with and to attach any callback implementations to.
     */
    private <V> KrakenRequestBuilder<V> getRequestBuilder(final TypeAdapter<V> typeAdapter,
                                                          final String path,
                                                          final String method,
                                                          final String... data) {
        return new KrakenRequestBuilder<V>() {
            @Override
            public Request<V> enqueue() {
                return new DefaultRequest<>(() -> {
                    // Perform coarse API key+secret validation
                    if (isPrivateRequest(path) && (key == null || secret == null))
                        throw new IllegalStateException(
                                "An API key and secret is required for this request");

                    // Everything's peachy: prepare the Kraken request decoration
                    String nonce = generateNonce(path);
                    String message = composeMessage(nonce, data);
                    List<NetworkClient.Header> headers = generateSignature(path, nonce, message);

                    // Prepare the HTTP metrics depending on "get" or "post" method.
                    boolean isGetMethod = "get".equals(method.toLowerCase());
                    byte[] payload = isGetMethod ? null : asBytes(message);
                    String mime = isGetMethod ? null : "application/x-www-form-urlencoded";
                    String uri = isGetMethod ?
                            String.format("%s%s?%s", baseUrl, path, message) :
                            String.format("%s%s", baseUrl, path);

                    // Perform the actual request (the network client stems from
                    // CachedRequestBuilder (extended by KrakenRequestBuilder).
                    byte[] responseBytes = okHttpNetworkClient.execute(uri, method, headers, payload, mime);
                    String responseJson = asString(responseBytes);

                    // Parse the response JSON
                    GsonJsonParser parser = new GsonJsonParser();
                    if (typeAdapter != null)
                        parser.gsonBuilder.registerTypeAdapter(
                                KrakenResponse.class,
                                new KrakenTypeAdapter<>(typeAdapter));

                    KrakenResponse<V> response = parser.fromJson(responseJson);

                    // Throw exception if has error (to trigger error callbacks)...
                    if (response.error.length > 0)
                        throw new KrakenRequestException(response.error);

                    // ...or deliver result.
                    return response.result;
                });
            }
        };
    }

    /**
     * Checks whether a given url is targeting a private endpoint in the Kraken
     * API.
     *
     * @param url The url to check.
     * @return Boolean true if targeting a private endpoint, false otherwise.
     */
    private boolean isPrivateRequest(final String url) {
        return url.contains("/private/");
    }

    /**
     * Generates a nonce for private requests, or null for public requests.
     *
     * @return The nonce or null.
     */
    private String generateNonce(final String url) {
        return url != null && isPrivateRequest(url) ?
                String.format("%-16s", System.currentTimeMillis())
                        .replace(" ", "0") :
                null;
    }

    /**
     * Constructs an encoded request message that Kraken will understand. See
     * Kraken API documentation for details.
     *
     * @param nonce The request nonce. Ignored if null.
     * @param data  The actual key value pairs to build the message from. Even
     *              positions are treated as keys and odd positions as values.
     *              Any null pointer key or value will render the key/value pair
     *              invalid and hence ignored. Any trailing keys will also be
     *              ignored.
     * @return The prepared and encoded Kraken message.
     */
    private String composeMessage(final String nonce, final String... data) {
        Uri.Builder builder = new Uri.Builder();

        if (nonce != null)
            builder.appendQueryParameter("nonce", nonce);

        if (data != null && data.length > 1)
            for (int i = 0; i < data.length; i += 2)
                if (data[i] != null && data[i + 1] != null)
                    builder.appendQueryParameter(data[i], data[i + 1]);

        return builder.build().getEncodedQuery();
    }

    /**
     * Generates the message signature headers. See Kraken API documentation for
     * details.
     *
     * @param path    The request path.
     * @param nonce   The request nonce that was used for the message.
     * @param message The request message
     * @return The Kraken request signature headers. Null if no path is given or
     * an empty list if no nonce is given.
     */
    private List<NetworkClient.Header> generateSignature(final String path,
                                                         final String nonce,
                                                         final String message) {
        if (key == null || secret == null)
            return null;

        if (path == null)
            return null;

        if (nonce == null)
            return null;

        byte[] data = sha256(asBytes(nonce + message));
        if (data == null)
            return null;

        byte[] input = concat(asBytes(path), data);
        byte[] signature = hmacSha512(input, secret);
        if (signature == null)
            return null;

        List<NetworkClient.Header> headers = new ArrayList<>(2);
        headers.add(new NetworkClient.Header("API-Key", key));
        headers.add(new NetworkClient.Header("API-Sign", base64Encode(signature)));
        return headers;
    }

}
