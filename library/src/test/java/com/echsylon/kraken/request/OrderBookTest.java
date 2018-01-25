package com.echsylon.kraken.request;

import com.echsylon.atlantis.Atlantis;
import com.echsylon.kraken.Dictionary;
import com.echsylon.kraken.dto.Depth;

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
 * These test cases will test the "order book (market depth)" feature of the
 * Android Kraken SDK.
 * <p>
 * The tests will take advantage of the fact that the Kraken implementation
 * returns a {@code Request} object. Since the {@code Request}
 * class extends {@code FutureTask} we can block the test thread until a result
 * is produced.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = 25)
public class OrderBookTest {

    private Atlantis atlantis;

    @After
    public void after() {
        atlantis.stop();
        atlantis = null;
    }


    @Test
    public void requestingDepth_shouldReturnMapOfParsedDepthObject() throws Exception {
        atlantis = startMockServer("GET", "/0/public/Depth",
                "{'error': [], 'result': {" +
                        " 'XETHZEUR': {" +
                        "  'asks': [" +
                        "    ['271.13309','2.368',1503523308]," +
                        "    ['271.13317','0.300',1503521093]]," +
                        "  'bids': [" +
                        "    ['271.07985','2.750',1503523300]," +
                        "    ['271.00001','1.157',1503523300]]}}}");

        Dictionary<Depth> result = getKrakenInstance()
                .getOrderBook(null)
                .enqueue()
                .get(1, SECONDS);

        assertThat(result.size(), is(1));

        Depth depth = result.get("XETHZEUR");
        assertThat(depth.asks.length, is(2));
        assertThat(depth.asks[0].price, is("271.13309"));
        assertThat(depth.asks[0].volume, is("2.368"));
        assertThat(depth.asks[0].timestamp, is(1503523308L));
        assertThat(depth.asks[1].price, is("271.13317"));
        assertThat(depth.asks[1].volume, is("0.300"));
        assertThat(depth.asks[1].timestamp, is(1503521093L));
        assertThat(depth.bids.length, is(2));
        assertThat(depth.bids[0].price, is("271.07985"));
        assertThat(depth.bids[0].volume, is("2.750"));
        assertThat(depth.bids[0].timestamp, is(1503523300L));
        assertThat(depth.bids[1].price, is("271.00001"));
        assertThat(depth.bids[1].volume, is("1.157"));
        assertThat(depth.bids[1].timestamp, is(1503523300L));
    }

}
