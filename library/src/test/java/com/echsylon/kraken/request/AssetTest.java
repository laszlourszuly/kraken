package com.echsylon.kraken.request;

import com.echsylon.atlantis.Atlantis;
import com.echsylon.blocks.callback.DefaultRequest;
import com.echsylon.kraken.Dictionary;
import com.echsylon.kraken.dto.Asset;

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
 * These test cases will test the "assets" feature of the Android Kraken SDK.
 * <p>
 * The tests will take advantage of the fact that the Kraken implementation
 * returns a {@code DefaultRequest} object. Since the {@code DefaultRequest}
 * class extends {@code FutureTask} we can block the test thread until a
 * result is produced.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = 25)
public class AssetTest {

    private Atlantis atlantis;

    @After
    public void after() {
        atlantis.stop();
        atlantis = null;
    }


    @Test
    public void requestingAssets_shouldReturnMapOfParsedAssetObjects() throws Exception {
        atlantis = startMockServer("GET", "/0/public/Assets",
                "{'error': [], 'result': {" +
                        " 'XYZ': {" +
                        " 'aclass': 'test_class'," +
                        " 'altname': 'XYZ'," +
                        " 'decimals': 10," +
                        " 'display_decimals': 5}}}");

        Dictionary<Asset> result =
                ((DefaultRequest<Dictionary<Asset>>) getKrakenInstance()
                        .getAssetInfo()
                        .enqueue())
                        .get(1, SECONDS);

        assertThat(result.size(), is(1));

        Asset asset = result.get("XYZ");
        assertThat(asset.assetClass, is("test_class"));
        assertThat(asset.alternativeName, is("XYZ"));
        assertThat(asset.decimals, is(10));
        assertThat(asset.displayDecimals, is(5));
    }

}
