package com.echsylon.kraken;

import com.echsylon.kraken.dto.Trade;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * This class knows how to parse Kraken recent trade JSON into a corresponding
 * Java object.
 * <p>
 * For technical details on the API see the online documentation:
 * https://www.kraken.com/help/api
 */
final class TradeTypeAdapter extends TypeAdapter<KrakenList<Trade>> {

    @Override
    public void write(JsonWriter jsonWriter, KrakenList<Trade> value) throws IOException {
        // We're not supposed to write trades JSON.
    }

    @Override
    public KrakenList<Trade> read(JsonReader in) throws IOException {
        KrakenList<Trade> result = new KrakenList<>();
        in.beginObject();   // Enter "result" object

        while (in.hasNext()) {
            switch (in.nextName()) {
                case "last":
                    result.last = in.nextString();
                    break;

                default:
                    // We're only expecting one asset pair entry. If there are any more,
                    // these are ignored.
                    if (!result.isEmpty()) {
                        in.skipValue();
                        continue;
                    }

                    // Read all data tuples
                    in.beginArray();
                    while (in.hasNext()) {
                        Trade recent = new Trade();
                        in.beginArray();
                        recent.price = in.nextString();
                        recent.volume = in.nextString();
                        recent.time = in.nextDouble();
                        recent.buyOrSell = in.nextString();
                        recent.marketOrLimit = in.nextString();
                        recent.misc = in.nextString();
                        in.endArray();
                        result.add(recent);
                    }
                    in.endArray();
                    in.endObject();
                    break;
            }
        }
        return result;
    }

}
