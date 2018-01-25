package com.echsylon.kraken.request;

import com.echsylon.atlantis.Atlantis;
import com.echsylon.kraken.dto.OrderCancelReceipt;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.echsylon.kraken.TestHelper.getKrakenInstance;
import static com.echsylon.kraken.TestHelper.startMockServer;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * These test cases will test the "cancel open order" feature of the Android
 * Kraken SDK.
 * <p>
 * The tests will take advantage of the fact that the Kraken implementation
 * returns a {@code Request} object. Since the {@code Request}
 * class extends {@code FutureTask} we can block the test thread until a result
 * is produced.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = 25)
public class CancelOrderTest {

    private Atlantis atlantis;

    @After
    public void after() {
        atlantis.stop();
        atlantis = null;
    }


    @Test
    public void cancellingOpenOrder_shouldReturnParsedOrderReceiptObject() throws Exception {
        atlantis = startMockServer("POST", "/0/private/CancelOrder",
                "{'error': [], 'result': {" +
                        "'count': 1," +
                        "'pending': 'true'}}");

        String key = "key";
        String secret = "c2VjcmV0";

        OrderCancelReceipt result = getKrakenInstance(key, secret)
                .cancelOpenOrder(null)
                .enqueue()
                .get(1, SECONDS);

        assertThat(result.count, is(1));
        assertThat(result.pending, is(true));
    }

}
