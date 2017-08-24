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
        assertThat(ticker.ask.price, is("1"));
        assertThat(ticker.ask.wholeLotVolume, is("2"));
        assertThat(ticker.ask.lotVolume, is("3"));
        assertThat(ticker.bid.price, is("4"));
        assertThat(ticker.bid.wholeLotVolume, is("5"));
        assertThat(ticker.bid.lotVolume, is("6"));
        assertThat(ticker.lastClosedTrade.price, is("7"));
        assertThat(ticker.lastClosedTrade.lotVolume, is("8"));
        assertThat(ticker.volume.today, is("9"));
        assertThat(ticker.volume.last24Hours, is("10"));
        assertThat(ticker.volumeWeightedAveragePrice.today, is("11"));
        assertThat(ticker.volumeWeightedAveragePrice.last24Hours, is("12"));
        assertThat(ticker.numberOfTrades.today, is("13"));
        assertThat(ticker.numberOfTrades.last24Hours, is("14"));
        assertThat(ticker.low.today, is("15"));
        assertThat(ticker.low.last24Hours, is("16"));
        assertThat(ticker.high.today, is("17"));
        assertThat(ticker.high.last24Hours, is("18"));
        assertThat(ticker.openingPriceToday, is("19"));
    }

}
