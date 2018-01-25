package com.echsylon.kraken.request;

import com.echsylon.atlantis.Atlantis;
import com.echsylon.kraken.dto.Time;

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
 * These test cases will test the "server time" feature of the Android Kraken
 * SDK.
 * <p>
 * The tests will take advantage of the fact that the Kraken implementation
 * returns a {@code Request} object. Since the {@code Request}
 * class extends {@code FutureTask} we can block the test thread until a result
 * is produced.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = 25)
public class ServerTimeTest {

    private Atlantis atlantis;

    @After
    public void after() {
        atlantis.stop();
        atlantis = null;
    }


    @Test
    public void requestingServerTime_shouldReturnParsedTimeObject() throws Exception {
        atlantis = startMockServer("GET", "/0/public/Time",
                "{'error': [], 'result': {" +
                        " 'unixtime': 0," +
                        " 'rfc1123': ''}}");

        Time time = getKrakenInstance()
                .getServerTime()
                .enqueue()
                .get(1, SECONDS);

        assertThat(time.unixtime, is(0L));
        assertThat(time.rfc1123, is(""));
    }

}
