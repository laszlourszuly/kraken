package com.echsylon.kraken.request;

import com.echsylon.atlantis.Atlantis;
import com.echsylon.blocks.callback.DefaultRequest;
import com.echsylon.kraken.dto.DepositAddress;

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
 * These test cases will test the "deposit addresses" feature of the Android
 * Kraken SDK.
 * <p>
 * The tests will take advantage of the fact that the Kraken implementation
 * returns a {@code DefaultRequest} object. Since the {@code DefaultRequest}
 * class extends {@code FutureTask} we can block the test thread until a result
 * is produced.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = 25)
public class DepositAddressesTest {

    private Atlantis atlantis;

    @After
    public void after() {
        atlantis.stop();
        atlantis = null;
    }


    @Test
    public void requestingDepositAddresses_shouldReturnArrayOfParsedObjects() throws Exception {
        atlantis = startMockServer("POST", "/0/private/DepositAddresses",
                "{'error': [], 'result': [{" +
                        " 'address': '0x0000000000000000000000000000000000000000'," +
                        " 'expiretm': '0'," +
                        " 'new': true}]}");

        String key = "key";
        String secret = "c2VjcmV0";

        DepositAddress[] result =
                ((DefaultRequest<DepositAddress[]>) getKrakenInstance(key, secret)
                        .getDepositAddresses("asset", "method")
                        .enqueue())
                        .get(1, SECONDS);

        assertThat(result.length, is(1));
        assertThat(result[0].address, is("0x0000000000000000000000000000000000000000"));
        assertThat(result[0].expiryTime, is("0"));
        assertThat(result[0].isNew, is(true));
    }

}
