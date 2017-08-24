package com.echsylon.kraken;

import com.echsylon.atlantis.Atlantis;
import com.echsylon.blocks.callback.DefaultRequest;
import com.echsylon.kraken.dto.Spread;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * These test cases will test the "recent spread" feature of the Android Kraken
 * SDK.
 * <p>
 * The tests will take advantage of the fact that the Kraken implementation
 * returns a {@code DefaultRequest} object. Since the {@code DefaultRequest}
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
        atlantis = MockHelper.start("GET", "/0/public/Spread",
                "{'error': [], 'result': {" +
                        " 'XETHZEUR': [" +
                        "  [1503526467,'271.13909','271.49000']," +
                        "  [1503526475,'271.17987','271.49001']]," +
                        " 'last':1503526481}}");

        DefaultRequest<Dictionary<Spread[]>> request =
                (DefaultRequest<Dictionary<Spread[]>>) new Kraken("http://localhost:8080")
                        .getRecentSpreadData(null, null)
                        .enqueue();

        Dictionary<Spread[]> result = request.get(); // Blocks until Kraken delivers
        assertThat(result.size(), is(1));
        assertThat(result.last, is("1503526481"));

        Spread[] trades = result.get("XETHZEUR");
        assertThat(trades[0].time, is(1503526467L));
        assertThat(trades[0].bid, is("271.13909"));
        assertThat(trades[0].ask, is("271.49000"));
        assertThat(trades[1].time, is(1503526475L));
        assertThat(trades[1].bid, is("271.17987"));
        assertThat(trades[1].ask, is("271.49001"));
    }

}
