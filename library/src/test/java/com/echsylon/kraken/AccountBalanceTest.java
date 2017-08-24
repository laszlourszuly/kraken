package com.echsylon.kraken;

import com.echsylon.atlantis.Atlantis;
import com.echsylon.blocks.callback.DefaultRequest;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * These test cases will test the "account balance" feature of the Android
 * Kraken SDK.
 * <p>
 * The tests will take advantage of the fact that the Kraken implementation
 * returns a {@code DefaultRequest} object. Since the {@code DefaultRequest}
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
        atlantis = MockHelper.start("POST", "/0/private/Balance",
                "{'error': [], 'result': {" +
                        " 'ZEUR': '0.0000'," +
                        " 'XETH': '21.1589468600'}}");

        String key = "key";
        String secret = "c2VjcmV0";

        DefaultRequest<Dictionary<String>> request =
                (DefaultRequest<Dictionary<String>>) new Kraken("http://localhost:8080", key, secret)
                        .getAccountBalance()
                        .enqueue();

        Dictionary<String> result = request.get(); // Blocks until Kraken delivers
        assertThat(result.size(), is(2));

        assertThat(result.get("ZEUR"), is("0.0000"));
        assertThat(result.get("XETH"), is("21.1589468600"));
    }

}
