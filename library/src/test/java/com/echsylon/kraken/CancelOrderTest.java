package com.echsylon.kraken;

import com.echsylon.atlantis.Atlantis;
import com.echsylon.blocks.callback.DefaultRequest;
import com.echsylon.kraken.dto.OrderCancelReceipt;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * These test cases will test the "cancel open order" feature of the Android
 * Kraken SDK.
 * <p>
 * The tests will take advantage of the fact that the Kraken implementation
 * returns a {@code DefaultRequest} object. Since the {@code DefaultRequest}
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
        atlantis = MockHelper.start("POST", "/0/private/CancelOrder",
                "{'error': [], 'result': {" +
                        "'count': 1," +
                        "'pending': 'true'}}");

        String key = "key";
        String secret = "c2VjcmV0";

        DefaultRequest<OrderCancelReceipt> request =
                (DefaultRequest<OrderCancelReceipt>) new Kraken("http://localhost:8080", key, secret)
                        .cancelOpenOrder(null)
                        .enqueue();

        OrderCancelReceipt result = request.get(); // Blocks until Kraken delivers
        assertThat(result.count, is(1));
        assertThat(result.pending, is(true));
    }

}
