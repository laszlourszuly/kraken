package com.echsylon.kraken.request;

import com.echsylon.atlantis.Atlantis;
import com.echsylon.blocks.callback.DefaultRequest;
import com.echsylon.kraken.dto.TradeBalance;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.echsylon.kraken.TestHelper.getKrakenInstance;
import static com.echsylon.kraken.TestHelper.startMockServer;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * These test cases will test the "trade balance" feature of the Android Kraken
 * SDK.
 * <p>
 * The tests will take advantage of the fact that the Kraken implementation
 * returns a {@code DefaultRequest} object. Since the {@code DefaultRequest}
 * class extends {@code FutureTask} we can block the test thread until a result
 * is produced.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = 25)
public class TradeBalanceTest {

    private Atlantis atlantis;

    @After
    public void after() {
        atlantis.stop();
        atlantis = null;
    }


    @Test
    public void requestingTradeBalance_shouldReturnParsedTradeBalanceObjects() throws Exception {
        atlantis = startMockServer("POST", "/0/private/TradeBalance",
                "{'error': [], 'result': {" +
                        " 'eb': '21.1589470825'," +
                        " 'tb': '21.1589468600'," +
                        " 'm': '0.0000000001'," +
                        " 'n': '0.0000000002'," +
                        " 'c': '0.0000000003'," +
                        " 'v': '0.0000000004'," +
                        " 'e': '21.1589468600'," +
                        " 'mf': '21.1589468605'}}");

        String key = "key";
        String secret = "c2VjcmV0";

        TradeBalance result =
                ((DefaultRequest<TradeBalance>) getKrakenInstance(key, secret)
                        .getTradeBalance()
                        .enqueue())
                        .get(1, SECONDS);

        assertThat(result.equivalentBalance, is("21.1589470825"));
        assertThat(result.tradeBalance, is("21.1589468600"));
        assertThat(result.margin, is("0.0000000001"));
        assertThat(result.net, is("0.0000000002"));
        assertThat(result.cost, is("0.0000000003"));
        assertThat(result.floatingValuation, is("0.0000000004"));
        assertThat(result.equity, is("21.1589468600"));
        assertThat(result.freeMargin, is("21.1589468605"));
        assertThat(result.marginLevel, is(nullValue()));
    }

}
