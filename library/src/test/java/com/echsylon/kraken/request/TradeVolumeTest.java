package com.echsylon.kraken.request;

import com.echsylon.atlantis.Atlantis;
import com.echsylon.blocks.callback.DefaultRequest;
import com.echsylon.kraken.dto.TradeVolume;

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
 * These test cases will test the "trade volume" feature of the Android Kraken
 * SDK.
 * <p>
 * The tests will take advantage of the fact that the Kraken implementation
 * returns a {@code DefaultRequest} object. Since the {@code DefaultRequest}
 * class extends {@code FutureTask} we can block the test thread until a result
 * is produced.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = 25)
public class TradeVolumeTest {

    private Atlantis atlantis;

    @After
    public void after() {
        atlantis.stop();
        atlantis = null;
    }


    @Test
    public void requestingTradeVolumes_shouldReturnParsedTradeVolumeObject() throws Exception {
        atlantis = startMockServer("POST", "/0/private/TradeVolume",
                "{'error': [], 'result': {" +
                        "'currency': 'ZUSD'," +
                        "'volume': '146820.7852'," +
                        "'fees': {" +
                        "  'XETHZEUR': {" +
                        "    'fee': '0.2200'," +
                        "    'minfee': '0.1000'," +
                        "    'maxfee': '0.2600'," +
                        "    'nextfee': '0.2000'," +
                        "    'nextvolume': '250000.0000'," +
                        "    'tiervolume': '100000.0000'}}," +
                        "'fees_maker': {" +
                        "  'XETHZEUR': {" +
                        "    'fee': '0.1200'," +
                        "    'minfee': '0.0000'," +
                        "    'maxfee': '0.1600'," +
                        "    'nextfee': '0.1000'," +
                        "    'nextvolume': '250000.0000'," +
                        "    'tiervolume': '100000.0000'}}}}");

        String key = "key";
        String secret = "c2VjcmV0";

        TradeVolume result =
                ((DefaultRequest<TradeVolume>) getKrakenInstance(key, secret)
                        .getTradeVolume()
                        .enqueue())
                        .get(1, SECONDS);

        assertThat(result.currency, is("ZUSD"));
        assertThat(result.volume, is("146820.7852"));
        assertThat(result.fees.size(), is(1));
        assertThat(result.makerFees.size(), is(1));

        TradeVolume.FeeInfo feeInfo;

        feeInfo = result.fees.get("XETHZEUR");
        assertThat(feeInfo.fee, is("0.2200"));
        assertThat(feeInfo.minFee, is("0.1000"));
        assertThat(feeInfo.maxFee, is("0.2600"));
        assertThat(feeInfo.nextFee, is("0.2000"));
        assertThat(feeInfo.nextVolume, is("250000.0000"));
        assertThat(feeInfo.tierVolume, is("100000.0000"));

        feeInfo = result.makerFees.get("XETHZEUR");
        assertThat(feeInfo.fee, is("0.1200"));
        assertThat(feeInfo.minFee, is("0.0000"));
        assertThat(feeInfo.maxFee, is("0.1600"));
        assertThat(feeInfo.nextFee, is("0.1000"));
        assertThat(feeInfo.nextVolume, is("250000.0000"));
        assertThat(feeInfo.tierVolume, is("100000.0000"));
    }

}
