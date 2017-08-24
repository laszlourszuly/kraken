package com.echsylon.kraken;

import com.echsylon.atlantis.Atlantis;
import com.echsylon.blocks.callback.DefaultRequest;
import com.echsylon.kraken.dto.OrderAddReceipt;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * These test cases will test the "add order" feature of the Android Kraken SDK.
 * <p>
 * The tests will take advantage of the fact that the Kraken implementation
 * returns a {@code DefaultRequest} object. Since the {@code DefaultRequest}
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
        atlantis = MockHelper.start("POST", "/0/private/AddOrder",
                "{'error': [], 'result': {" +
                        "'descr': {" +
                        "  'order': 'sell 1.00000000 ETHEUR @ limit 400.00000'}," +
                        "'txid': [" +
                        "  'ORDER-TRANSACTION-ID']}}");

        String key = "key";
        String secret = "c2VjcmV0";

        DefaultRequest<OrderAddReceipt> request =
                (DefaultRequest<OrderAddReceipt>) new Kraken("http://localhost:8080", key, secret)
                        .addStandardOrder(null, null, null, null, null, null, null, null, null, null, null, null, null, null)
                        .enqueue();

        OrderAddReceipt result = request.get(); // Blocks until Kraken delivers
        OrderAddReceipt.Description description = result.descr;
        assertThat(description.order, is("sell 1.00000000 ETHEUR @ limit 400.00000"));
        assertThat(description.close, is(nullValue()));
        assertThat(result.txId.length, is(1));
        assertThat(result.txId[0], is("ORDER-TRANSACTION-ID"));
    }

}
