package com.echsylon.kraken;

import com.echsylon.atlantis.Atlantis;
import com.echsylon.blocks.callback.DefaultRequest;
import com.echsylon.kraken.dto.Ledger;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * These test cases will test the "ledgers" feature of the Android Kraken SDK.
 * <p>
 * The tests will take advantage of the fact that the Kraken implementation
 * returns a {@code DefaultRequest} object. Since the {@code DefaultRequest}
 * class extends {@code FutureTask} we can block the test thread until a
 * result is produced.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = 25)
public class LedgerTest {

    private Atlantis atlantis;

    @After
    public void after() {
        atlantis.stop();
        atlantis = null;
    }


    @Test
    public void requestingLedgersInfo_shouldReturnParsedAndUnwrappedLedgerObjects() throws Exception {
        atlantis = MockHelper.start("POST", "/0/private/Ledgers",
                "{'error': [], 'result': {" +
                        "'ledger': {" +
                        "  'LEDGER-ID': {" +
                        "    'refid': 'REFERENCE-ID'," +
                        "    'time': 1503088479.9367," +
                        "    'type': 'withdrawal'," +
                        "    'aclass': 'currency'," +
                        "    'asset': 'XETH'," +
                        "    'amount': '-10.0000000000'," +
                        "    'fee': '0.0050000000'," +
                        "    'balance': '21.1589468600'}}," +
                        "'count':521}}");

        String key = "key";
        String secret = "c2VjcmV0";

        DefaultRequest<Dictionary<Ledger>> request =
                (DefaultRequest<Dictionary<Ledger>>) new Kraken("http://localhost:8080", key, secret)
                        .getLedgersInfo(null, null, null, null, null)
                        .enqueue();

        Dictionary<Ledger> result = request.get(); // Blocks until Kraken delivers
        assertThat(result.size(), is(1));
        assertThat(result.count, is(521));
        assertThat(result.last, is(nullValue()));

        Ledger ledger = result.get("LEDGER-ID");
        assertThat(ledger.refId, is("REFERENCE-ID"));
        assertThat(ledger.time, is(1503088479.9367D));
        assertThat(ledger.type, is("withdrawal"));
        assertThat(ledger.aClass, is("currency"));
        assertThat(ledger.asset, is("XETH"));
        assertThat(ledger.amount, is("-10.0000000000"));
        assertThat(ledger.fee, is("0.0050000000"));
        assertThat(ledger.balance, is("21.1589468600"));
    }

}
