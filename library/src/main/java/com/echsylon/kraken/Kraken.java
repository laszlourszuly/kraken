package com.echsylon.kraken;

import com.echsylon.blocks.network.OkHttpNetworkClient;
import com.echsylon.kraken.internal.CallCounter;
import com.echsylon.kraken.request.AccountBalanceRequestBuilder;
import com.echsylon.kraken.request.AddOrderRequestBuilder;
import com.echsylon.kraken.request.AssetInfoRequestBuilder;
import com.echsylon.kraken.request.CancelOrderRequestBuilder;
import com.echsylon.kraken.request.ClosedOrdersRequestBuilder;
import com.echsylon.kraken.request.DepositAddressesRequestBuilder;
import com.echsylon.kraken.request.DepositMethodsRequestBuilder;
import com.echsylon.kraken.request.DepositStatusesRequestBuilder;
import com.echsylon.kraken.request.LedgersRequestBuilder;
import com.echsylon.kraken.request.OhlcDataRequestBuilder;
import com.echsylon.kraken.request.OpenOrdersRequestBuilder;
import com.echsylon.kraken.request.OpenPositionsRequestBuilder;
import com.echsylon.kraken.request.OrderBookRequestBuilder;
import com.echsylon.kraken.request.QueryLedgersRequestBuilder;
import com.echsylon.kraken.request.QueryOrdersRequestBuilder;
import com.echsylon.kraken.request.QueryTradesRequestBuilder;
import com.echsylon.kraken.request.RecentSpreadRequestBuilder;
import com.echsylon.kraken.request.RecentTradesRequestBuilder;
import com.echsylon.kraken.request.ServerTimeRequestBuilder;
import com.echsylon.kraken.request.TickerInfoRequestBuilder;
import com.echsylon.kraken.request.TradableAssetPairsRequestBuilder;
import com.echsylon.kraken.request.TradeBalanceRequestBuilder;
import com.echsylon.kraken.request.TradeHistoryRequestBuilder;
import com.echsylon.kraken.request.TradeVolumeRequestBuilder;
import com.echsylon.kraken.request.WithdrawInfoRequestBuilder;
import com.echsylon.kraken.request.WithdrawRequestBuilder;
import com.echsylon.kraken.request.WithdrawStatusesRequestBuilder;

import java.io.File;

import static com.echsylon.kraken.internal.Utils.base64Decode;

/**
 * This class describes the Kraken API.
 * <p>
 * For further technical details see Kraken API documentation at:
 * https://www.kraken.com/help/api
 */
@SuppressWarnings("WeakerAccess")
public class Kraken {
    private static final String BASE_URL = "https://api.kraken.com";
    private static CallCounter callCounter;

    /**
     * Initiates the Kraken API Client local cache metrics.
     *
     * @param cacheDirectory The location on disk to save the cache in.
     * @param cacheSizeBytes The max number of bytes to allocate to caching.
     */
    public static void setupCache(final File cacheDirectory, final int cacheSizeBytes) {
        OkHttpNetworkClient.settings(
                new OkHttpNetworkClient.Settings(
                        cacheDirectory,
                        cacheSizeBytes,
                        false,          // Don't follow http -> http redirects
                        false));        // Don't follow https -> http redirects
    }

    /**
     * Initiate the automatic call rate limit management.
     *
     * @param tier The tier that decides which call rate limits to apply. Note
     *             that the server has the last say regarding what tier the
     *             account actually has.
     */
    public static void setCallRateLimit(int tier) {
        callCounter = new CallCounter(tier);
    }

    /**
     * Cancel the automatic call rate limit management.
     */
    public static void clearCallRateLimit() {
        callCounter = null;
    }


    private String baseUrl;
    private String key;
    private byte[] secret;

    /**
     * Allows test cases to redirect requests to a test environment. Not exposed
     * to user to keep false assumptions away.
     *
     * @param baseUrl The base url to the test environment.
     */
    Kraken(String baseUrl, String key, String secret) {
        this.baseUrl = baseUrl;
        this.key = key;
        this.secret = base64Decode(secret);
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

    // Public data API

    /**
     * Retrieves the current server time. This is to aid in approximating the
     * skew time between the server and client.
     *
     * @return A request builder object to configure the request and any client
     * side cache metrics with, and to attach any callback implementations to.
     */
    public ServerTimeRequestBuilder getServerTime() {
        return new ServerTimeRequestBuilder(callCounter, baseUrl, key, secret);
    }

    /**
     * Retrieves information about supported currency assets. See API
     * documentation on supported asset formats.
     *
     * @return A request builder object to configure the request and any client
     * side cache metrics with, and to attach any callback implementations to.
     */
    public AssetInfoRequestBuilder getAssetInfo() {
        return new AssetInfoRequestBuilder(callCounter, baseUrl, key, secret);
    }

    /**
     * Retrieves information about tradable asset pairs. See API documentation
     * on supported asset pair formats.
     *
     * @return A request builder object to configure the request and any client
     * side cache metrics with, and to attach any callback implementations to.
     */
    public TradableAssetPairsRequestBuilder getTradableAssetPairs() {
        return new TradableAssetPairsRequestBuilder(callCounter, baseUrl, key, secret);
    }

    /**
     * Retrieves information about the ticker state for tradable asset pairs.
     * The user is expected to provide at least one asset pair.
     *
     * @param pairs The asset pairs to fetch information on.
     * @return A request builder object to configure the request and any client
     * side cache metrics with, and to attach any callback implementations to.
     */
    public TickerInfoRequestBuilder getTickerInformation(final String... pairs) {
        return new TickerInfoRequestBuilder(callCounter, baseUrl, key, secret)
                .useAssetPairs(pairs);
    }

    /**
     * Retrieves information about the Open/High/Low/Close state for a tradable
     * asset pair. The user is expected to provide at least one asset pair.
     *
     * @param pair The single asset pair to get information for.
     * @return A request builder object to configure the request and any client
     * side cache metrics with, and to attach any callback implementations to.
     */
    public OhlcDataRequestBuilder getOhlcData(final String pair) {
        return new OhlcDataRequestBuilder(callCounter, baseUrl, key, secret)
                .useAssetPair(pair);
    }

    /**
     * Retrieves the market depth for tradable asset pairs. The user is expected
     * to provide at least one asset pair.
     *
     * @param pair The asset pair to get market depth for.
     * @return A request builder object to configure the request and any client
     * side cache metrics with, and to attach any callback implementations to.
     */
    public OrderBookRequestBuilder getOrderBook(final String pair) {
        return new OrderBookRequestBuilder(callCounter, baseUrl, key, secret)
                .useAssetPair(pair);
    }

    /**
     * Retrieves recent trades on the market. The user is expected to provide at
     * least one asset pair.
     *
     * @param pair The asset pair to get data for.
     * @return A request builder object to configure the request and any client
     * side cache metrics with, and to attach any callback implementations to.
     */
    public RecentTradesRequestBuilder getRecentTrades(final String pair) {
        return new RecentTradesRequestBuilder(callCounter, baseUrl, key, secret)
                .useAssetPair(pair);
    }

    /**
     * Retrieves information about recent spread data for a tradable asset pair.
     * The user is expected to provide at least one asset pair.
     *
     * @param pair The asset pair to get information for.
     * @return A request builder object to configure the request and any client
     * side cache metrics with, and to attach any callback implementations to.
     */
    public RecentSpreadRequestBuilder getRecentSpreadData(final String pair) {
        return new RecentSpreadRequestBuilder(callCounter, baseUrl, key, secret)
                .useAssetPair(pair);
    }

    // Private user data API

    /**
     * Retrieves the account balance for all associated assets.
     *
     * @return A request builder object to configure the request and any client
     * side cache metrics with, and to attach any callback implementations to.
     */
    public AccountBalanceRequestBuilder getAccountBalance() {
        return new AccountBalanceRequestBuilder(callCounter, baseUrl, key, secret);
    }

    /**
     * Retrieves the trade balance of an asset.
     *
     * @return A request builder object to configure the request and any client
     * side cache metrics with, and to attach any callback implementations to.
     */
    public TradeBalanceRequestBuilder getTradeBalance() {
        return new TradeBalanceRequestBuilder(callCounter, baseUrl, key, secret);
    }

    /**
     * Retrieves information about any open orders.
     *
     * @return A request builder object to configure the request and any client
     * side cache metrics with, and to attach any callback implementations to.
     */
    public OpenOrdersRequestBuilder getOpenOrders() {
        return new OpenOrdersRequestBuilder(callCounter, baseUrl, key, secret);
    }

    /**
     * Retrieves information about any closed orders.
     *
     * @return A request builder object to configure the request and any client
     * side cache metrics with, and to attach any callback implementations to.
     */
    public ClosedOrdersRequestBuilder getClosedOrders() {
        return new ClosedOrdersRequestBuilder(callCounter, baseUrl, key, secret);
    }

    /**
     * Retrieves information about any particular order(s). The user is expected
     * to provide at least one transaction id.
     *
     * @param transactionIds Transaction ids of the orders to get info about. 20
     *                       max, at least one is required.
     * @return A request builder object to configure the request and any client
     * side cache metrics with, and to attach any callback implementations to.
     */
    public QueryOrdersRequestBuilder queryOrdersInfo(final String... transactionIds) {
        return new QueryOrdersRequestBuilder(callCounter, baseUrl, key, secret)
                .useTransactions(transactionIds);
    }

    /**
     * Retrieves trades history
     *
     * @return A request builder object to configure the request and any client
     * side cache metrics with, and to attach any callback implementations to.
     */
    public TradeHistoryRequestBuilder getTradesHistory() {
        return new TradeHistoryRequestBuilder(callCounter, baseUrl, key, secret);
    }

    /**
     * Retrieves information about any particular trades(s). The user is
     * expected to provide at least one transaction id.
     *
     * @param transactionIds Transaction ids of the orders to get info about. 20
     *                       max, at least one is required.
     * @return A request builder object to configure the request and any client
     * side cache metrics with, and to attach any callback implementations to.
     */
    public QueryTradesRequestBuilder queryTradesInfo(final String... transactionIds) {
        return new QueryTradesRequestBuilder(callCounter, baseUrl, key, secret)
                .useTransactions(transactionIds);
    }

    /**
     * Retrieves information about any open positions
     *
     * @return A request builder object to configure the request and any client
     * side cache metrics with, and to attach any callback implementations to.
     */
    public OpenPositionsRequestBuilder getOpenPositions() {
        return new OpenPositionsRequestBuilder(callCounter, baseUrl, key, secret);
    }

    /**
     * Retrieves information about the ledgers.
     *
     * @return A request builder object to configure the request and any client
     * side cache metrics with, and to attach any callback implementations to.
     */
    public LedgersRequestBuilder getLedgersInfo() {
        return new LedgersRequestBuilder(callCounter, baseUrl, key, secret);
    }

    /**
     * Retrieves information about any particular ledger(s). The user is
     * expected to provide at least one ledger id.
     *
     * @param ledgerIds Id of ledgers to get. 20 max, at least one is required.
     * @return A request builder object to configure the request and any client
     * side cache metrics with, and to attach any callback implementations to.
     */
    public QueryLedgersRequestBuilder queryLedgers(final String... ledgerIds) {
        return new QueryLedgersRequestBuilder(callCounter, baseUrl, key, secret)
                .useLedgers(ledgerIds);
    }

    /**
     * Retrieves information about the current trade volumes.
     *
     * @return A request builder object to configure the request and any client
     * side cache metrics with, and to attach any callback implementations to.
     */
    public TradeVolumeRequestBuilder getTradeVolume() {
        return new TradeVolumeRequestBuilder(callCounter, baseUrl, key, secret);
    }

    // Private user trading

    /**
     * Adds a regular sell or buy order
     *
     * @return A request builder object to configure the request and any client
     * side cache metrics with, and to attach any callback implementations to.
     */
    public AddOrderRequestBuilder addStandardOrder(final String pair,
                                                   final String type,
                                                   final String orderType,
                                                   final String price) {
        return new AddOrderRequestBuilder(callCounter, baseUrl, key, secret)
                .useAssetPair(pair)
                .useType(type)
                .useOrderType(orderType)
                .usePrice(price);
    }

    /**
     * Cancels an open order.
     *
     * @param id The transaction or user reference id of the order(s) to close.
     * @return A request builder object to configure any client side cache
     * metrics with and to attach any callback implementations to.
     */
    public CancelOrderRequestBuilder cancelOpenOrder(final String id) {
        return new CancelOrderRequestBuilder(callCounter, baseUrl, key, secret)
                .useOrderId(id);
    }

    // Private user funding - tentative API

    /**
     * Retrieves available funding methods for a currency (defaults to ZUSD).
     *
     * @return A request builder object to configure any client side cache
     * metrics with and to attach any callback implementations to.
     */
    public DepositMethodsRequestBuilder getDepositMethods() {
        return new DepositMethodsRequestBuilder(callCounter, baseUrl, key, secret);
    }

    /**
     * Retrieves available funding addresses for a currency.
     *
     * @param asset  The deposit asset.
     * @param method The name of the deposit method.
     * @return A request builder object to configure any client side cache
     * metrics with and to attach any callback implementations to.
     */
    public DepositAddressesRequestBuilder getDepositAddresses(final String asset,
                                                              final String method) {
        return new DepositAddressesRequestBuilder(callCounter, baseUrl, key, secret)
                .useAsset(asset)
                .useMethod(method);
    }

    /**
     * Retrieves information on recent deposit statuses.
     *
     * @param asset The deposit asset.
     * @return A request builder object to configure any client side cache
     * metrics with and to attach any callback implementations to.
     */
    public DepositStatusesRequestBuilder getDepositStatuses(final String asset) {
        return new DepositStatusesRequestBuilder(callCounter, baseUrl, key, secret)
                .useAsset(asset);
    }

    /**
     * Retrieves information about a possible withdrawal.
     *
     * @param asset    The asset to withdraw.
     * @param receiver The receiving key of the withdrawal.
     * @param amount   The amount of assets to withdraw.
     * @return A request builder object to configure any client side cache
     * metrics with and to attach any callback implementations to.
     */
    public WithdrawInfoRequestBuilder getWithdrawInfo(final String asset,
                                                      final String receiver,
                                                      final float amount) {
        return new WithdrawInfoRequestBuilder(callCounter, baseUrl, key, secret)
                .useAsset(asset)
                .useKey(receiver)
                .useAmount(amount);
    }

    /**
     * Retrieves information about recent withdrawal statuses.
     *
     * @param asset The withdrawal asset.
     * @return A request builder object to configure any client side cache
     * metrics with and to attach any callback implementations to.
     */
    public WithdrawStatusesRequestBuilder getWithdrawStatuses(final String asset) {
        return new WithdrawStatusesRequestBuilder(callCounter, baseUrl, key, secret)
                .useAsset(asset);
    }

    /**
     * Places a withdrawal order.
     *
     * @param asset    The asset to withdraw.
     * @param receiver The receiving key of the withdrawal.
     * @param amount   The amount of assets to withdraw.
     * @return A request builder object to configure any client side cache
     * metrics with and to attach any callback implementations to.
     */
    public WithdrawRequestBuilder withdrawFunds(final String asset,
                                                final String receiver,
                                                final float amount) {
        return new WithdrawRequestBuilder(callCounter, baseUrl, key, secret)
                .useAsset(asset)
                .useKey(receiver)
                .useAmount(amount);
    }

}
