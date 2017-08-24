package com.echsylon.kraken;

import com.echsylon.atlantis.Atlantis;
import com.echsylon.blocks.callback.DefaultRequest;
import com.echsylon.kraken.dto.Trade;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * These test cases will test the "recent trades" feature of the Android Kraken
 * SDK.
 * <p>
 * The tests will take advantage of the fact that the Kraken implementation
 * returns a {@code DefaultRequest} object. Since the {@code DefaultRequest}
 * class extends {@code FutureTask} we can block the test thread until a result
 * is produced.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = 25)
public class RecentTradesTest {

    private Atlantis atlantis;

    @After
    public void after() {
        atlantis.stop();
        atlantis = null;
    }


    @Test
    public void requestingRecentTrades_shouldReturnMapOfParsedTradesObjects() throws Exception {
        atlantis = MockHelper.start("GET", "/0/public/Trades",
                "{'error': [], 'result': {" +
                        " 'XETHZEUR': [" +
                        "  ['271.49021','0.72000000',1503524391.0341,'s','l','']," +
                        "  ['271.49001','0.10043200',1503524391.3286,'b','l','']]," +
                        " 'last':'1503524404183915423'}}");

        DefaultRequest<Dictionary<Trade[]>> request =
                (DefaultRequest<Dictionary<Trade[]>>) new Kraken("http://localhost:8080")
                        .getRecentTrades(null, null)
                        .enqueue();

        Dictionary<Trade[]> result = request.get(); // Blocks until Kraken delivers
        assertThat(result.size(), is(1));
        assertThat(result.last, is("1503524404183915423"));

        Trade[] trades = result.get("XETHZEUR");
        assertThat(trades[0].price, is("271.49021"));
        assertThat(trades[0].volume, is("0.72000000"));
        assertThat(trades[0].time, is(1503524391.0341D));
        assertThat(trades[0].buyOrSell, is("s"));
        assertThat(trades[0].marketOrLimit, is("l"));
        assertThat(trades[0].misc, is(""));
        assertThat(trades[1].price, is("271.49001"));
        assertThat(trades[1].volume, is("0.10043200"));
        assertThat(trades[1].time, is(1503524391.3286D));
        assertThat(trades[1].buyOrSell, is("b"));
        assertThat(trades[1].marketOrLimit, is("l"));
        assertThat(trades[1].misc, is(""));
    }

}
