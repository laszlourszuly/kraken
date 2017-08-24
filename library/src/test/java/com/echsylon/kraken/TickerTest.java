package com.echsylon.kraken;

import com.echsylon.atlantis.Atlantis;
import com.echsylon.blocks.callback.DefaultRequest;
import com.echsylon.kraken.dto.Ticker;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * These test cases will test the "ticker" feature of the Android Kraken SDK.
 * <p>
 * The tests will take advantage of the fact that the Kraken implementation
 * returns a {@code DefaultRequest} object. Since the {@code DefaultRequest}
 * class extends {@code FutureTask} we can block the test thread until a
 * result is produced.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = 25)
public class TickerTest {

    private Atlantis atlantis;

    @After
    public void after() {
        atlantis.stop();
        atlantis = null;
    }


    @Test
    public void requestingTicker_shouldReturnMapOfParsedTickerObjects() throws Exception {
        atlantis = MockHelper.start("GET", "/0/public/Ticker",
                "{'error': [], 'result': {" +
                        " 'XETHZEUR': {" +
                        "  'a': ['1','2','3']," +
                        "  'b': ['4','5','6']," +
                        "  'c': ['7','8']," +
                        "  'v': ['9','10']," +
                        "  'p': ['11','12']," +
                        "  't': [13,14]," +
                        "  'l': ['15','16']," +
                        "  'h': ['17','18']," +
                        "  'o': '19'}}}");

        DefaultRequest<Dictionary<Ticker>> request =
                (DefaultRequest<Dictionary<Ticker>>) new Kraken("http://localhost:8080")
                        .getTickerInformation()
                        .enqueue();

        Dictionary<Ticker> result = request.get(); // Blocks until Kraken delivers
        assertThat(result.size(), is(1));

        Ticker ticker = result.get("XETHZEUR");
        assertThat(ticker.a.price, is("1"));
        assertThat(ticker.a.wholeLotVolume, is("2"));
        assertThat(ticker.a.lotVolume, is("3"));
        assertThat(ticker.b.price, is("4"));
        assertThat(ticker.b.wholeLotVolume, is("5"));
        assertThat(ticker.b.lotVolume, is("6"));
        assertThat(ticker.c.price, is("7"));
        assertThat(ticker.c.lotVolume, is("8"));
        assertThat(ticker.v.today, is("9"));
        assertThat(ticker.v.last24Hours, is("10"));
        assertThat(ticker.p.today, is("11"));
        assertThat(ticker.p.last24Hours, is("12"));
        assertThat(ticker.t.today, is("13"));
        assertThat(ticker.t.last24Hours, is("14"));
        assertThat(ticker.l.today, is("15"));
        assertThat(ticker.l.last24Hours, is("16"));
        assertThat(ticker.h.today, is("17"));
        assertThat(ticker.h.last24Hours, is("18"));
        assertThat(ticker.o, is("19"));
    }

}
