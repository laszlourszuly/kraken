package com.echsylon.kraken;

import com.echsylon.atlantis.Atlantis;
import com.echsylon.blocks.callback.DefaultRequest;
import com.echsylon.kraken.dto.TradeHistory;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * These test cases will test the "query trades" feature of the Android Kraken
 * SDK.
 * <p>
 * The tests will take advantage of the fact that the Kraken implementation
 * returns a {@code DefaultRequest} object. Since the {@code DefaultRequest}
 * class extends {@code FutureTask} we can block the test thread until a result
 * is produced.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = 25)
public class QueryTradesTest {

    private Atlantis atlantis;

    @After
    public void after() {
        atlantis.stop();
        atlantis = null;
    }


    @Test
    public void queryingArbitraryTrades_shouldReturnParsedTradeHistoryObjects() throws Exception {
        atlantis = MockHelper.start("POST", "/0/private/QueryTrades",
                "{'error': [], 'result': {" +
                        "'TRANSACTION-ID': {" +
                        "  'ordertxid': 'ORDER-ID'," +
                        "  'pair': 'XETHZEUR'," +
                        "  'time': 1502402208.9073," +
                        "  'type': 'buy'," +
                        "  'ordertype': 'limit'," +
                        "  'price': '260.00000'," +
                        "  'cost': '8785.09240'," +
                        "  'fee': '10.54211'," +
                        "  'vol': '33.78881692'," +
                        "  'margin': '0.00000'," +
                        "  'misc': ''}}}");

        String key = "key";
        String secret = "c2VjcmV0";

        DefaultRequest<Dictionary<TradeHistory>> request =
                (DefaultRequest<Dictionary<TradeHistory>>) new Kraken("http://localhost:8080", key, secret)
                        .queryTradesInfo(null)
                        .enqueue();

        Dictionary<TradeHistory> result = request.get(); // Blocks until Kraken delivers
        assertThat(result.size(), is(1));
        assertThat(result.last, is(nullValue()));
        assertThat(result.count, is(nullValue()));

        TradeHistory tradeHistory = result.get("TRANSACTION-ID");
        assertThat(tradeHistory.orderTxId, is("ORDER-ID"));
        assertThat(tradeHistory.positionStatus, is(nullValue()));
        assertThat(tradeHistory.pair, is("XETHZEUR"));
        assertThat(tradeHistory.time, is(1502402208.9073D));
        assertThat(tradeHistory.type, is("buy"));
        assertThat(tradeHistory.orderType, is("limit"));
        assertThat(tradeHistory.price, is("260.00000"));
        assertThat(tradeHistory.cPrice, is(nullValue()));
        assertThat(tradeHistory.cost, is("8785.09240"));
        assertThat(tradeHistory.cCost, is(nullValue()));
        assertThat(tradeHistory.fee, is("10.54211"));
        assertThat(tradeHistory.cFee, is(nullValue()));
        assertThat(tradeHistory.vol, is("33.78881692"));
        assertThat(tradeHistory.cVol, is(nullValue()));
        assertThat(tradeHistory.margin, is("0.00000"));
        assertThat(tradeHistory.cMargin, is(nullValue()));
        assertThat(tradeHistory.misc, is(""));
        assertThat(tradeHistory.net, is(nullValue()));
        assertThat(tradeHistory.trades, is(nullValue()));
    }

}
