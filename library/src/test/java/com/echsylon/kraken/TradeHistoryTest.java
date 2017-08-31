package com.echsylon.kraken;

import com.echsylon.atlantis.Atlantis;
import com.echsylon.blocks.callback.DefaultRequest;
import com.echsylon.kraken.dto.TradeHistory;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * These test cases will test the "trade history" feature of the Android Kraken
 * SDK.
 * <p>
 * The tests will take advantage of the fact that the Kraken implementation
 * returns a {@code DefaultRequest} object. Since the {@code DefaultRequest}
 * class extends {@code FutureTask} we can block the test thread until a result
 * is produced.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = 25)
public class TradeHistoryTest {

    private Atlantis atlantis;

    @After
    public void after() {
        atlantis.stop();
        atlantis = null;
    }


    @Test
    public void requestingTradesHistory_shouldReturnParsedAndUnwrappedTradeHistoryObjects() throws Exception {
        atlantis = MockHelper.start("POST", "/0/private/TradesHistory",
                "{'error': [], 'result': {" +
                        "'trades': {" +
                        "  'TRANSACTION-ID': {" +
                        "    'ordertxid': 'ORDER-ID'," +
                        "    'pair': 'XETHZEUR'," +
                        "    'time': 1502402208.9073," +
                        "    'type': 'buy'," +
                        "    'ordertype': 'limit'," +
                        "    'price': '260.00000'," +
                        "    'cost': '8785.09240'," +
                        "    'fee': '10.54211'," +
                        "    'vol': '33.78881692'," +
                        "    'margin': '0.00000'," +
                        "    'misc': ''}}," +
                        "'count': 252}}");

        String key = "key";
        String secret = "c2VjcmV0";

        Dictionary<TradeHistory> result =
                ((DefaultRequest<Dictionary<TradeHistory>>) new Kraken("http://localhost:8080", key, secret)
                        .getTradesHistory()
                        .enqueue())
                        .get(1, SECONDS);

        assertThat(result.size(), is(1));
        assertThat(result.count, is(252));
        assertThat(result.last, is(nullValue()));

        TradeHistory tradeHistory = result.get("TRANSACTION-ID");
        assertThat(tradeHistory.orderTransactionId, is("ORDER-ID"));
        assertThat(tradeHistory.positionStatus, is(nullValue()));
        assertThat(tradeHistory.pair, is("XETHZEUR"));
        assertThat(tradeHistory.time, is(1502402208.9073D));
        assertThat(tradeHistory.type, is("buy"));
        assertThat(tradeHistory.orderType, is("limit"));
        assertThat(tradeHistory.price, is("260.00000"));
        assertThat(tradeHistory.closedPartPrice, is(nullValue()));
        assertThat(tradeHistory.cost, is("8785.09240"));
        assertThat(tradeHistory.closedPartCost, is(nullValue()));
        assertThat(tradeHistory.fee, is("10.54211"));
        assertThat(tradeHistory.closedPartFee, is(nullValue()));
        assertThat(tradeHistory.volume, is("33.78881692"));
        assertThat(tradeHistory.closedPartVolume, is(nullValue()));
        assertThat(tradeHistory.margin, is("0.00000"));
        assertThat(tradeHistory.closedPartMargin, is(nullValue()));
        assertThat(tradeHistory.misc, is(""));
        assertThat(tradeHistory.net, is(nullValue()));
        assertThat(tradeHistory.trades, is(nullValue()));
    }

}
