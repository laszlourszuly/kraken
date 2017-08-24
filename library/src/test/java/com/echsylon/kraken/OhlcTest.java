package com.echsylon.kraken;

import com.echsylon.atlantis.Atlantis;
import com.echsylon.blocks.callback.DefaultRequest;
import com.echsylon.kraken.dto.Ohlc;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * These test cases will test the "OHLC" feature of the Android Kraken SDK.
 * <p>
 * The tests will take advantage of the fact that the Kraken implementation
 * returns a {@code DefaultRequest} object. Since the {@code DefaultRequest}
 * class extends {@code FutureTask} we can block the test thread until a
 * result is produced.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = 25)
public class OhlcTest {

    private Atlantis atlantis;

    @After
    public void after() {
        atlantis.stop();
        atlantis = null;
    }


    @Test
    public void requestingOhlc_shouldReturnMapOfParsedOhlcObjects() throws Exception {
        atlantis = MockHelper.start("GET", "/0/public/OHLC",
                "{'error': [], 'result': {" +
                        " 'XETHZEUR': [[" +
                        "   1503405000," +
                        "  '268.00050'," +
                        "  '270.02758'," +
                        "  '267.51370'," +
                        "  '269.00106'," +
                        "  '268.70391'," +
                        "  '1365.04869100'," +
                        "   616]]," +
                        " 'last': 1503403200}}");

        DefaultRequest<Dictionary<Ohlc[]>> request =
                (DefaultRequest<Dictionary<Ohlc[]>>) new Kraken("http://localhost:8080")
                        .getOhlcData(null, null, null)
                        .enqueue();

        Dictionary<Ohlc[]> result = request.get(); // Blocks until Kraken delivers
        assertThat(result.size(), is(1));
        assertThat(result.last, is("1503403200"));

        Ohlc ohlc = result.get("XETHZEUR")[0];
        assertThat(ohlc.time, is(1503405000L));
        assertThat(ohlc.open, is("268.00050"));
        assertThat(ohlc.high, is("270.02758"));
        assertThat(ohlc.low, is("267.51370"));
        assertThat(ohlc.close, is("269.00106"));
        assertThat(ohlc.volumeWeightedAveragePrice, is("268.70391"));
        assertThat(ohlc.volume, is("1365.04869100"));
        assertThat(ohlc.count, is(616));
    }

}
