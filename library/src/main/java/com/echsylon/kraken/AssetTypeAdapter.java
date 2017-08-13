package com.echsylon.kraken;

import com.echsylon.kraken.dto.Asset;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class knows how to parse Kraken market depth JSON into a corresponding
 * Java object.
 * <p>
 * For technical details on the API see the online documentation:
 * https://www.kraken.com/help/api
 */
final class AssetTypeAdapter extends TypeAdapter<List<Asset>> {

    @Override
    public void write(JsonWriter out, List<Asset> value) throws IOException {
        // We're not supposed to write asset JSON.
    }

    @Override
    public List<Asset> read(JsonReader in) throws IOException {
        List<Asset> result = new ArrayList<>();
        in.beginObject();   // Enter "result" object

        while (in.hasNext()) {
            Asset entry = new Asset();
            in.nextName();  // Silently consume asset name
            in.beginObject();

            while (in.hasNext()) {
                switch (in.nextName()) {
                    case "aclass":
                        entry.aClass = in.nextString();
                        break;
                    case "altname":
                        entry.altName = in.nextString();
                        break;
                    case "decimals":
                        entry.decimals = in.nextInt();
                        break;
                    case "display_decimals":
                        entry.displayDecimals = in.nextInt();
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
