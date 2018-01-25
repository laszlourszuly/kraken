package com.echsylon.kraken.request;

import com.echsylon.atlantis.Atlantis;
import com.echsylon.kraken.dto.WithdrawInfo;

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
public class WithdrawInfoTest {

    private Atlantis atlantis;

    @After
    public void after() {
        atlantis.stop();
        atlantis = null;
    }


    @Test
    public void requestingWithdrawInfo_shouldReturnParsedObject() throws Exception {
        atlantis = startMockServer("POST", "/0/private/WithdrawInfo",
                "{'error': [], 'result': {" +
                        " 'method': 'Ether'," +
                        " 'limit': '1.0000000000'," +
                        " 'amount': '2.0000000000'," +
                        " 'fee': '0.0050000000'}}");

        String key = "key";
        String secret = "c2VjcmV0";

        WithdrawInfo result = getKrakenInstance(key, secret)
                .getWithdrawInfo("asset", "key", 1.0f)
                .enqueue()
                .get(1, SECONDS);

        assertThat(result.method, is("Ether"));
        assertThat(result.limit, is("1.0000000000"));
        assertThat(result.netTotal, is("2.0000000000"));
        assertThat(result.fee, is("0.0050000000"));
    }

}
