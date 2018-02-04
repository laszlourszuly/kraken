package com.echsylon.kraken.request;

import com.echsylon.atlantis.Atlantis;
import com.echsylon.kraken.Dictionary;
import com.echsylon.kraken.dto.Spread;

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
 * These test cases will test the "recent spread" feature of the Android Kraken
 * SDK.
 * <p>
 * The tests will take advantage of the fact that the Kraken implementation
 * returns a {@code Request} object. Since the {@code Request}
 * class extends {@code FutureTask} we can block the test thread until a result
 * is produced.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = 25)
public class RecentSpreadTest {

    private Atlantis atlantis;

    @After
    public void after() {
        atlantis.stop();
        atlantis = null;
    }


    @Test
    public void requestingRecentSpread_shouldReturnMapOfParsedSpreadObjects() throws Exception {
        atlantis = startMockServer("GET", "/0/public/Spread",
                "{'error': [], 'result': {" +
                        " 'XETHZEUR': [" +
                        "  [1503526467,'271.13909','271.49000']," +
                        "  [1503526475,'271.17987','271.49001']]," +
                        " 'last':1503526481}}");

        Dictionary<Spread[]> result = getKrakenInstance()
                .getRecentSpreadData(null)
                .enqueue()
                .get(10, SECONDS);

        assertThat(result.size(), is(1));
        assertThat(result.last, is("1503526481"));

        Spread[] trades = result.get("XETHZEUR");
        assertThat(trades[0].timestamp, is(1503526467L));
        assertThat(trades[0].bid, is("271.13909"));
        assertThat(trades[0].ask, is("271.49000"));
        assertThat(trades[1].timestamp, is(1503526475L));
        assertThat(trades[1].bid, is("271.17987"));
        assertThat(trades[1].ask, is("271.49001"));
    }

}
