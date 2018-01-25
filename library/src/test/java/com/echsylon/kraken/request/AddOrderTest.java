package com.echsylon.kraken.request;

import com.echsylon.atlantis.Atlantis;
import com.echsylon.kraken.dto.OrderAddReceipt;

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
 * These test cases will test the "add order" feature of the Android Kraken SDK.
 * <p>
 * The tests will take advantage of the fact that the Kraken implementation
 * returns a {@code Request} object. Since the {@code Request}
 * class extends {@code FutureTask} we can block the test thread until a
 * result is produced.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = 25)
public class AddOrderTest {

    private Atlantis atlantis;

    @After
    public void after() {
        atlantis.stop();
        atlantis = null;
    }


    @Test
    public void addingStandardOrder_shouldReturnParsedOrderReceiptObject() throws Exception {
        atlantis = startMockServer("POST", "/0/private/AddOrder",
                "{'error': [], 'result': {" +
                        "'descr': {" +
                        "  'order': 'sell 1.00000000 ETHEUR @ limit 400.00000'}," +
                        "'txid': [" +
                        "  'ORDER-TRANSACTION-ID']}}");

        String key = "key";
        String secret = "c2VjcmV0";

        OrderAddReceipt result = getKrakenInstance(key, secret)
                .addStandardOrder(null, null, null, null)
                .enqueue()
                .get(1, SECONDS);

        OrderAddReceipt.Description description = result.description;
        assertThat(description.order, is("sell 1.00000000 ETHEUR @ limit 400.00000"));
        assertThat(description.close, is(nullValue()));
        assertThat(result.transactionId.length, is(1));
        assertThat(result.transactionId[0], is("ORDER-TRANSACTION-ID"));
    }

}
