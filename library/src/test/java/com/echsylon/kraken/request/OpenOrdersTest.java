package com.echsylon.kraken.request;

import com.echsylon.atlantis.Atlantis;
import com.echsylon.kraken.Dictionary;
import com.echsylon.kraken.dto.Order;

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
 * These test cases will test the "open orders" feature of the Android Kraken
 * SDK.
 * <p>
 * The tests will take advantage of the fact that the Kraken implementation
 * returns a {@code Request} object. Since the {@code Request}
 * class extends {@code FutureTask} we can block the test thread until a result
 * is produced.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = 25)
public class OpenOrdersTest {

    private Atlantis atlantis;

    @After
    public void after() {
        atlantis.stop();
        atlantis = null;
    }


    @Test
    public void requestingOpenOrders_shouldReturnParsedAndUnwrappedOrderObjects() throws Exception {
        atlantis = startMockServer("POST", "/0/private/OpenOrders",
                "{'error': [], 'result': {" +
                        "'open': {" +
                        "  'ORDER-ID': {" +
                        "    'refid': '1'," +
                        "    'userref': '2'," +
                        "    'status': 'some_status'," +
                        "    'reason': 'User did something'," +
                        "    'opentm': 1495104207.9914," +
                        "    'closetm': 1495161357.6272," +
                        "    'starttm': 0," +
                        "    'expiretm': 0," +
                        "    'descr': {" +
                        "      'pair': 'ETHEUR'," +
                        "      'type': 'buy'," +
                        "      'ordertype': 'limit'," +
                        "      'price': '82.00000'," +
                        "      'price2': '0'," +
                        "      'leverage': 'none'," +
                        "      'order': 'buy 20.73170000 ETHEUR @ limit 82.00000'}," +
                        "    'vol': '20.73170000'," +
                        "    'vol_exec': '0.00000001'," +
                        "    'cost': '0.00002'," +
                        "    'fee': '0.00003'," +
                        "    'price': '0.00004'," +
                        "    'misc': ''," +
                        "    'oflags': 'some,flags'}}}}");

        String key = "key";
        String secret = "c2VjcmV0";

        Dictionary<Order> result = getKrakenInstance(key, secret)
                .getOpenOrders()
                .enqueue()
                .get(10, SECONDS);

        assertThat(result.size(), is(1));
        assertThat(result.last, is(nullValue()));
        assertThat(result.count, is(nullValue()));

        Order order = result.get("ORDER-ID");
        assertThat(order.referenceId, is("1"));
        assertThat(order.userReference, is("2"));
        assertThat(order.status, is("some_status"));
        assertThat(order.reason, is("User did something"));
        assertThat(order.openTime, is(1495104207.9914D));
        assertThat(order.closeTime, is(1495161357.6272D));
        assertThat(order.startTime, is(0D));
        assertThat(order.expireTime, is(0D));
        assertThat(order.volume, is("20.73170000"));
        assertThat(order.executedVolume, is("0.00000001"));
        assertThat(order.cost, is("0.00002"));
        assertThat(order.fee, is("0.00003"));
        assertThat(order.price, is("0.00004"));
        assertThat(order.stopPrice, is(nullValue()));
        assertThat(order.limitPrice, is(nullValue()));
        assertThat(order.misc, is(""));
        assertThat(order.orderFlags, is("some,flags"));
        assertThat(order.trades, is(nullValue()));

        Order.Description description = order.description;
        assertThat(description.pair, is("ETHEUR"));
        assertThat(description.type, is("buy"));
        assertThat(description.orderType, is("limit"));
        assertThat(description.price, is("82.00000"));
        assertThat(description.secondaryPrice, is("0"));
        assertThat(description.leverage, is("none"));
        assertThat(description.order, is("buy 20.73170000 ETHEUR @ limit 82.00000"));
        assertThat(description.close, is(nullValue()));
    }

}
