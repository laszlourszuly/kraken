package com.echsylon.kraken;

import com.echsylon.kraken.dto.AssetPair;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class knows how to parse Kraken asset pair JSON into a corresponding
 * Java object.
 * <p>
 * For technical details on the API see the online documentation:
 * https://www.kraken.com/help/api
 */
final class AssetPairTypeAdapter extends TypeAdapter<List<AssetPair>> {

    @Override
    public void write(JsonWriter out, List<AssetPair> value) throws IOException {
        // We're not supposed to write asset JSON.
    }

    @Override
    public List<AssetPair> read(JsonReader in) throws IOException {
        List<AssetPair> result = new ArrayList<>();
        in.beginObject();   // Enter "result" object

        while (in.hasNext()) {
            AssetPair entry = new AssetPair();
            in.nextName();  // Silently consume asset name
            in.beginObject();

            while (in.hasNext()) {
                switch (in.nextName()) {
                    case "altname":
                        entry.altName = in.nextString();
                        break;
                    case "aclass_base":
                        entry.aClassBase = in.nextString();
                        break;
                    case "base":
                        entry.base = in.nextString();
                        break;
                    case "aclass_quote":
                        entry.aClassQuote = in.nextString();
                        break;
                    case "quote":
                        entry.quote = in.nextString();
                        break;
                    case "lot":
                        entry.lot = in.nextInt();
                        break;
                    case "pair_decimals":
                        entry.pairDecimals = in.nextInt();
                        break;
                    case "lot_decimals":
                        entry.lotDecimals = in.nextInt();
                        break;
                    case "lot_multiplier":
                        entry.lotMultiplier = in.nextInt();
                        break;
                    case "leverage_buy":
                        ArrayList<Integer> leverageBuyValues = new ArrayList<>();
                        in.beginArray();
                        while (in.hasNext())
                            leverageBuyValues.add(in.nextInt());
                        in.endArray();
                        entry.leverageBuy = new Integer[leverageBuyValues.size()];
                        leverageBuyValues.toArray(entry.leverageBuy);
                        break;
                    case "leverage_sell":// []
                        ArrayList<Integer> leverageSellValues = new ArrayList<>();
                        in.beginArray();
                        while (in.hasNext())
                            leverageSellValues.add(in.nextInt());
                        in.endArray();
                        entry.leverageSell = new Integer[leverageSellValues.size()];
                        leverageSellValues.toArray(entry.leverageBuy);
                        break;
                    case "fees":
                        ArrayList<AssetPair.FeeSchedule> fees = new ArrayList<>();
                        in.beginArray();
                        while (in.hasNext()) {
                            AssetPair.FeeSchedule fee = new AssetPair.FeeSchedule();
                            in.beginArray();
                            fee.volume = in.nextInt();
                            fee.feePercent = in.nextDouble();
                            in.endArray();
                            fees.add(fee);
                        }
                        in.endArray();
                        entry.fees = new AssetPair.FeeSchedule[fees.size()];
                        fees.toArray(entry.fees);
                        break;
                    case "fees_maker":// [[0.0, 0.16]]
                        ArrayList<AssetPair.FeeSchedule> feesMaker = new ArrayList<>();
                        in.beginArray();
                        while (in.hasNext()) {
                            AssetPair.FeeSchedule feeMaker = new AssetPair.FeeSchedule();
                            in.beginArray();
                            feeMaker.volume = in.nextInt();
                            feeMaker.feePercent = in.nextDouble();
                            in.endArray();
                            feesMaker.add(feeMaker);
                        }
                        in.endArray();
                        entry.feesMaker = new AssetPair.FeeSchedule[feesMaker.size()];
                        feesMaker.toArray(entry.fees);
                        break;
                    case "fee_volume_currency":
                        entry.feeVolumeCurrency = in.nextString();
                        break;
                    case "margin_call":
                        entry.marginCall = in.nextInt();
                        break;
                    case "margin_stop":
                        entry.marginStop = in.nextInt();
                        break;
                    default:
                        in.skipValue();
                        break;
                }
            }

            in.endObject();
            result.add(entry);
        }

        in.endObject();
        return result;
    }

}
