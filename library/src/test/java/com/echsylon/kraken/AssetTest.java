package com.echsylon.kraken;

import com.echsylon.atlantis.Atlantis;
import com.echsylon.blocks.callback.DefaultRequest;
import com.echsylon.kraken.dto.Asset;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

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
        atlantis = MockHelper.start("GET", "/0/public/Assets\\?.*",
                "{'error': [], 'result': {" +
                        " 'XYZ': {" +
                        " 'aclass': 'test_class'," +
                        " 'altname': 'XYZ'," +
                        " 'decimals': 10," +
                        " 'display_decimals': 5}}}");

        DefaultRequest<Dictionary<Asset>> request =
                (DefaultRequest<Dictionary<Asset>>) new Kraken("http://localhost:8080")
                        .getAssetInfo("info", "currency")
                        .enqueue();

        Dictionary<Asset> result = request.get(1, SECONDS); // Blocks until Kraken delivers
        assertThat(result.size(), is(1));

        Asset asset = result.get("XYZ");
        assertThat(asset.aClass, is("test_class"));
        assertThat(asset.altName, is("XYZ"));
        assertThat(asset.decimals, is(10));
        assertThat(asset.displayDecimals, is(5));
    }

}
