package com.echsylon.kraken;

import com.echsylon.kraken.dto.Ohlc;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * This class knows how to parse Kraken OHLC JSON into a corresponding
 * Java object.
 * <p>
 * For technical details on the API see the online documentation:
 * https://www.kraken.com/help/api
 */
final class OhlcTypeAdapter extends TypeAdapter<KrakenList<Ohlc>> {

    @Override
    public void write(JsonWriter out, KrakenList<Ohlc> value) throws IOException {
        // We're not supposed to write OHLC JSON.
    }

    @Override
    public KrakenList<Ohlc> read(JsonReader in) throws IOException {
        KrakenList<Ohlc> result = new KrakenList<>();
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
                        Ohlc entry = new Ohlc();
                        in.beginArray();
                        entry.time = in.nextLong();
                        entry.open = in.nextString();
                        entry.high = in.nextString();
                        entry.low = in.nextString();
                        entry.close = in.nextString();
                        entry.vwap = in.nextString();
                        entry.volume = in.nextString();
                        entry.count = in.nextInt();
                        in.endArray();
                        result.add(entry);
                    }
                    in.endArray();
            }
        }

        in.endObject();
        return result;
    }

}
