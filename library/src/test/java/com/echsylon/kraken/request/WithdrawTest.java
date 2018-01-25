package com.echsylon.kraken.request;

import com.echsylon.atlantis.Atlantis;
import com.echsylon.kraken.dto.WithdrawReceipt;

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
 * These test cases will test the "withdraw info" feature of the Android
 * Kraken SDK.
 * <p>
 * The tests will take advantage of the fact that the Kraken implementation
 * returns a {@code Request} object. Since the {@code Request}
 * class extends {@code FutureTask} we can block the test thread until a result
 * is produced.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = 25)
public class WithdrawTest {

    private Atlantis atlantis;

    @After
    public void after() {
        atlantis.stop();
        atlantis = null;
    }


    @Test
    public void requestingWithdraw_shouldReturnParsedObject() throws Exception {
        atlantis = startMockServer("POST", "/0/private/Withdraw",
                "{'error': [], 'result': {'refid': 'AABCDEF-123456-GHIJKL'}}");

        String key = "key";
        String secret = "c2VjcmV0";

        WithdrawReceipt result = getKrakenInstance(key, secret)
                .withdrawFunds("asset", "key", 1.0f)
                .enqueue()
                .get(1, SECONDS);

        assertThat(result.referenceId, is("AABCDEF-123456-GHIJKL"));
    }

}
