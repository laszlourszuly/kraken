package com.echsylon.kraken.request;


import com.echsylon.atlantis.Atlantis;
import com.echsylon.kraken.Dictionary;
import com.echsylon.kraken.dto.AssetPair;

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
 * These test cases will test the "asset pair" feature of the Android Kraken
 * SDK.
 * <p>
 * The tests will take advantage of the fact that the Kraken implementation
 * returns a {@code Request} object. Since the {@code Request}
 * class extends {@code FutureTask} we can block the test thread until a result
 * is produced.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = 25)
public class AssetPairTest {

    private Atlantis atlantis;

    @After
    public void after() {
        atlantis.stop();
        atlantis = null;
    }


    @Test
    public void requestingAssetPairs_shouldReturnMapOfParsedAssetPairObjects() throws Exception {
        atlantis = startMockServer("GET", "/0/public/AssetPairs",
                "{'error': [], 'result': {" +
                        " 'XETHZEUR': {" +
                        "  'altname': 'ETHEUR'," +
                        "  'aclass_base': 'currency'," +
                        "  'base': 'XETH'," +
                        "  'aclass_quote': 'currency'," +
                        "  'quote': 'ZEUR'," +
                        "  'lot': 'unit'," +
                        "  'pair_decimals': 5," +
                        "  'lot_decimals': 8," +
                        "  'lot_multiplier': 1," +
                        "  'leverage_buy': [2,3]," +
                        "  'leverage_sell': [7,8]," +
                        "  'fees': [[0,0.26],[5,0.24]]," +
                        "  'fees_maker': [[10,0.16],[50,0.14]]," +
                        "  'fee_volume_currency': 'ZUSD'," +
                        "  'margin_call': 80," +
                        "  'margin_stop': 40}}}");

        Dictionary<AssetPair> result = getKrakenInstance()
                .getTradableAssetPairs()
                .enqueue()
                .get(1, SECONDS);

        assertThat(result.size(), is(1));

        AssetPair assetPair = result.get("XETHZEUR");
        assertThat(assetPair.alternativeName, is("ETHEUR"));
        assertThat(assetPair.baseAssetClass, is("currency"));
        assertThat(assetPair.base, is("XETH"));
        assertThat(assetPair.quoteAssetClass, is("currency"));
        assertThat(assetPair.quote, is("ZEUR"));
        assertThat(assetPair.lot, is("unit"));
        assertThat(assetPair.pairDecimals, is(5));
        assertThat(assetPair.lotDecimals, is(8));
        assertThat(assetPair.lotMultiplier, is(1));
        assertThat(assetPair.leverageBuy.length, is(2));
        assertThat(assetPair.leverageBuy[0], is(2));
        assertThat(assetPair.leverageBuy[1], is(3));
        assertThat(assetPair.leverageSell.length, is(2));
        assertThat(assetPair.leverageSell[0], is(7));
        assertThat(assetPair.leverageSell[1], is(8));
        assertThat(assetPair.fees.length, is(2));
        assertThat(assetPair.fees[0].volume, is(0));
        assertThat(assetPair.fees[0].feePercent, is(0.26));
        assertThat(assetPair.fees[1].volume, is(5));
        assertThat(assetPair.fees[1].feePercent, is(0.24));
        assertThat(assetPair.makerFees.length, is(2));
        assertThat(assetPair.makerFees[0].volume, is(10));
        assertThat(assetPair.makerFees[0].feePercent, is(0.16));
        assertThat(assetPair.makerFees[1].volume, is(50));
        assertThat(assetPair.makerFees[1].feePercent, is(0.14));
        assertThat(assetPair.volumeCurrencyFee, is("ZUSD"));
        assertThat(assetPair.callMargin, is(80));
        assertThat(assetPair.stopMargin, is(40));
    }

}
