package com.echsylon.kraken.request;

import com.echsylon.atlantis.Atlantis;
import com.echsylon.kraken.dto.DepositStatus;

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
 * These test cases will test the "deposit statuses" feature of the Android
 * Kraken SDK.
 * <p>
 * The tests will take advantage of the fact that the Kraken implementation
 * returns a {@code Request} object. Since the {@code Request}
 * class extends {@code FutureTask} we can block the test thread until a result
 * is produced.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = 25)
public class DepositStatusesTest {

    private Atlantis atlantis;

    @After
    public void after() {
        atlantis.stop();
        atlantis = null;
    }


    @Test
    public void requestingDepositStatus_shouldReturnArrayOfParsedObjects() throws Exception {
        atlantis = startMockServer("POST", "/0/private/DepositStatus",
                "{'error': [], 'result': [{" +
                        " 'method': 'Ether (Hex)'," +
                        " 'aclass': 'currency'," +
                        " 'asset': 'XETH'," +
                        " 'refid': 'QABCDEF-123456-GHIJKL'," +
                        " 'txid': '0x0000000000000000000000000000000000000000000000000000000000000000'," +
                        " 'info': '0x0000000000000000000000000000000000000000'," +
                        " 'amount': '0.1234567890'," +
                        " 'fee': '0.0000000000'," +
                        " 'time': 0," +
                        " 'status': 'Success'," +
                        " 'status-prop': 'some-properties'}]}");

        String key = "key";
        String secret = "c2VjcmV0";

        DepositStatus[] result = getKrakenInstance(key, secret)
                .getDepositStatuses("asset")
                .enqueue()
                .get(10, SECONDS);

        assertThat(result.length, is(1));
        assertThat(result[0].method, is("Ether (Hex)"));
        assertThat(result[0].assetClass, is("currency"));
        assertThat(result[0].asset, is("XETH"));
        assertThat(result[0].referenceId, is("QABCDEF-123456-GHIJKL"));
        assertThat(result[0].transactionId, is("0x0000000000000000000000000000000000000000000000000000000000000000"));
        assertThat(result[0].transactionInfo, is("0x0000000000000000000000000000000000000000"));
        assertThat(result[0].depositedAmount, is("0.1234567890"));
        assertThat(result[0].fee, is("0.0000000000"));
        assertThat(result[0].timestamp, is(0L));
        assertThat(result[0].status, is("Success"));
        assertThat(result[0].properties, is("some-properties"));
    }

}
