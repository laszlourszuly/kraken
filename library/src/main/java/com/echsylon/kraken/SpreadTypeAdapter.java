package com.echsylon.kraken;

import com.echsylon.kraken.dto.Spread;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * This class knows how to parse Kraken spread JSON into a corresponding Java
 * object.
 * <p>
 * For technical details on the API see the online documentation:
 * https://www.kraken.com/help/api
 */
final class SpreadTypeAdapter extends TypeAdapter<KrakenList<Spread>> {

    @Override
    public void write(JsonWriter out, KrakenList<Spread> value) throws IOException {
        // We're not supposed to write spread JSON.
    }

    @Override
    public KrakenList<Spread> read(JsonReader in) throws IOException {
        KrakenList<Spread> result = new KrakenList<>();
        in.beginObject();   // Enter "result" object

        while (in.hasNext()) {
            switch (in.nextName()) {
                case "last":
                    result.last = in.nextString();
                    break;

                default:
                    // We're only expecting one result entry. If there are any more,
                    // these are ignored.
                    if (!result.isEmpty()) {
                        in.skipValue();
                        continue;
                    }

                    // Read all data tuples
                    in.beginArray();
                    while (in.hasNext()) {
                        Spread entry = new Spread();
                        in.beginArray();
                        entry.time = in.nextLong();
                        entry.bid = in.nextString();
                        entry.ask = in.nextString();
                        in.endArray();
                    }
                    in.endArray();
                    break;
            }
        }

        in.endObject();
        return result;
    }

}
