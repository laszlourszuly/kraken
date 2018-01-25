package com.echsylon.kraken.request;

import com.echsylon.atlantis.Atlantis;
import com.echsylon.kraken.Dictionary;

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
 * These test cases will test the "account balance" feature of the Android
 * Kraken SDK.
 * <p>
 * The tests will take advantage of the fact that the Kraken implementation
 * returns a {@code Request} object. Since the {@code Request}
 * class extends {@code FutureTask} we can block the test thread until a result
 * is produced.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = 25)
public class AccountBalanceTest {

    private Atlantis atlantis;

    @After
    public void after() {
        atlantis.stop();
        atlantis = null;
    }


    @Test
    public void requestingAccountBalance_shouldReturnMapOfParsedKeyValuePairs() throws Exception {
        atlantis = startMockServer("POST", "/0/private/Balance",
                "{'error': [], 'result': {" +
                        " 'ZEUR': '0.0000'," +
                        " 'XETH': '21.1589468600'}}");

        String key = "key";
        String secret = "c2VjcmV0";

        Dictionary<String> result = getKrakenInstance(key, secret)
                .getAccountBalance()
                .enqueue()
                .get(1, SECONDS);

        assertThat(result.size(), is(2));
        assertThat(result.get("ZEUR"), is("0.0000"));
        assertThat(result.get("XETH"), is("21.1589468600"));
    }

}
