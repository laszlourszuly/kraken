package com.echsylon.kraken;

import com.echsylon.atlantis.Atlantis;
import com.echsylon.kraken.dto.Time;
import com.echsylon.kraken.request.Request;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.concurrent.ExecutionException;

import static com.echsylon.kraken.TestHelper.getKrakenInstance;
import static com.echsylon.kraken.TestHelper.startMockServer;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * These test cases will test the core capabilities of the Android Kraken SDK.
 * <p>
 * The tests will take advantage of the fact that the Kraken implementation
 * returns a {@code Request} object. Since the {@code Request}
 * class extends {@code FutureTask} we can block the test thread until a result
 * is produced.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = 25)
public class KrakenTest {

    private Atlantis atlantis;

    @After
    public void after() {
        atlantis.stop();
        atlantis = null;
    }


    @Test
    public void responseWithError_shouldThrowException() throws Exception {
        atlantis = startMockServer("GET", "/0/public/Time",
                "{'error': ['Some:Error:Structure']}");

        Request<Time> request = getKrakenInstance()
                .getServerTime()
                .enqueue();

        assertThatThrownBy(() -> request.get(1, SECONDS))
                .isInstanceOf(ExecutionException.class)
                .hasCauseInstanceOf(KrakenRequestException.class);
    }

    @Test
    public void responseWithSuccess_shouldNotThrowException() throws Exception {
        atlantis = startMockServer("GET", "/0/public/Time",
                "{'error': [], 'result': {" +
                        " 'unixtime': 0," +
                        " 'rfc1123': 'some_rfc1123_time'}}");

        Request<Time> request = getKrakenInstance()
                .getServerTime()
                .enqueue();

        assertThat(request.get(1, SECONDS), is(notNullValue()));
    }

    @Test
    public void requestingPrivateResource_shouldThrowExceptionIfNoCredentialsProvided() throws Exception {
        atlantis = startMockServer("POST", "/0/private/Balance",
                "{'error': [], 'result': {}}");

        Request<?> request = getKrakenInstance()
                .getAccountBalance()
                .enqueue();

        assertThatThrownBy(() -> request.get(1, SECONDS))
                .isInstanceOf(ExecutionException.class)
                .hasCauseInstanceOf(IllegalStateException.class);
    }

    @Test
    public void requestingPrivateResource_shouldNotThrowExceptionIfCredentialsProvided() throws Exception {
        atlantis = startMockServer("POST", "/0/private/Balance",
                "{'error': [], result: {}}");

        String key = "key";
        String secret = "c2VjcmV0";

        Request<?> request = getKrakenInstance(key, secret)
                .getAccountBalance()
                .enqueue();

        assertThat(request.get(1, SECONDS), is(notNullValue()));
    }

    @Test
    public void performingTooFrequentRequests_shouldPauseProcessingRequests() throws Exception {
        atlantis = startMockServer("GET", "/0/public/Time",
                "{'error': [], 'result': {}}");

        try {
            Kraken.setCallRateLimit(2); // limit 15, reduced by 1 per 3 sec
            Kraken kraken = getKrakenInstance();
            for (int i = 0; i < 15; i++)
                kraken.getServerTime().enqueue();

            long startSeconds = System.currentTimeMillis() / 1000L;

            kraken.getServerTime()
                    .enqueue()
                    .get(4, SECONDS);

            long stopSeconds = System.currentTimeMillis() / 1000L;
            assertThat(stopSeconds - startSeconds, is(3L));
        } finally {
            Kraken.clearCallRateLimit();
        }
    }

    @Test
    public void performingTooFrequentRequestsAndThenWaiting_shouldChillCallCounterEnoughToAllowMoreRequests() throws Exception {
        atlantis = startMockServer("GET", "/0/public/Time",
                "{'error': [], 'result': {}}");

        try {
            Kraken.setCallRateLimit(2); // limit 15, reduced by 1 per 3 sec
            Kraken kraken = getKrakenInstance();
            for (int i = 0; i < 15; i++)
                kraken.getServerTime().enqueue();

            Thread.sleep(3000);

            long startSeconds = System.currentTimeMillis() / 1000L;

            kraken.getServerTime()
                    .enqueue()
                    .get(4, SECONDS);

            long stopSeconds = System.currentTimeMillis() / 1000L;
            assertThat(stopSeconds - startSeconds, is(0L));
        } finally {
            Kraken.clearCallRateLimit();
        }
    }
}
