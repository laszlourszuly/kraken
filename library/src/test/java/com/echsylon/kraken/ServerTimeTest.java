package com.echsylon.kraken;

import com.echsylon.atlantis.Atlantis;
import com.echsylon.blocks.callback.DefaultRequest;
import com.echsylon.kraken.dto.Time;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * These test cases will test the "server time" feature of the Android Kraken
 * SDK.
 * <p>
 * The tests will take advantage of the fact that the Kraken implementation
 * returns a {@code DefaultRequest} object. Since the {@code DefaultRequest}
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
        atlantis = MockHelper.start("GET", "/0/public/Time",
                "{'error': [], 'result': {" +
                        " 'unixtime': 0," +
                        " 'rfc1123': ''}}");

        DefaultRequest<Time> request =
                (DefaultRequest<Time>) new Kraken("http://localhost:8080")
                        .getServerTime()
                        .enqueue();

        Time time = request.get(1, SECONDS); // Blocks until Kraken delivers
        assertThat(time.unixtime, is(0L));
        assertThat(time.rfc1123, is(""));
    }

}
