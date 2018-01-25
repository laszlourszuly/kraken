package com.echsylon.kraken.request;

import com.echsylon.atlantis.Atlantis;
import com.echsylon.kraken.Dictionary;
import com.echsylon.kraken.dto.Position;

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
 * These test cases will test the "open positions" feature of the Android Kraken
 * SDK.
 * <p>
 * The tests will take advantage of the fact that the Kraken implementation
 * returns a {@code Request} object. Since the {@code Request}
 * class extends {@code FutureTask} we can block the test thread until a result
 * is produced.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = 25)
public class OpenPositionsTest {

    private Atlantis atlantis;

    @After
    public void after() {
        atlantis.stop();
        atlantis = null;
    }


    @Test
    public void requestingOpenPositions_shouldReturnParsedPositionObjects() throws Exception {
        atlantis = startMockServer("POST", "/0/private/OpenPositions",
                "{'error': [], 'result': {" +
                        "'TRANSACTION-ID': {" +
                        "  'ordertxid': 'ORDER-ID'," +
                        "  'pair': 'XETHZEUR'," +
                        "  'time': 1502402208.9073," +
                        "  'type': 'buy'," +
                        "  'ordertype': 'limit'," +
                        "  'cost': '8785.09240'," +
                        "  'fee': '10.54211'," +
                        "  'vol': '33.78881692'," +
                        "  'vol_closed': '33.78881693'," +
                        "  'margin': '0.00000'," +
                        "  'value': '0.00001'," +
                        "  'net': '0.2'," +
                        "  'misc': ''," +
                        "  'oflags': ''}}}");

        String key = "key";
        String secret = "c2VjcmV0";

        Dictionary<Position> result = getKrakenInstance(key, secret)
                .getOpenPositions()
                .enqueue()
                .get(1, SECONDS);

        assertThat(result.size(), is(1));
        assertThat(result.last, is(nullValue()));
        assertThat(result.count, is(nullValue()));

        Position position = result.get("TRANSACTION-ID");
        assertThat(position.orderTransactionId, is("ORDER-ID"));
        assertThat(position.pair, is("XETHZEUR"));
        assertThat(position.time, is(1502402208.9073D));
        assertThat(position.type, is("buy"));
        assertThat(position.orderType, is("limit"));
        assertThat(position.cost, is("8785.09240"));
        assertThat(position.fee, is("10.54211"));
        assertThat(position.volume, is("33.78881692"));
        assertThat(position.closedVolume, is("33.78881693"));
        assertThat(position.margin, is("0.00000"));
        assertThat(position.value, is("0.00001"));
        assertThat(position.net, is("0.2"));
        assertThat(position.misc, is(""));
        assertThat(position.orderFlags, is(""));
    }

}
