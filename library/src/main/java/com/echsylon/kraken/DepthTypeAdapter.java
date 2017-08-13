package com.echsylon.kraken;

import com.echsylon.kraken.dto.Depth;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * This class knows how to parse Kraken market depth JSON into a corresponding
 * Java object.
 * <p>
 * For technical details on the API see the online documentation:
 * https://www.kraken.com/help/api
 */
final class DepthTypeAdapter extends TypeAdapter<Depth> {

    @Override
    public void write(JsonWriter out, Depth value) throws IOException {
        // We're not supposed to write orders JSON.
    }

    @Override
    public Depth read(JsonReader in) throws IOException {
        Depth result = null;
        in.beginObject();   // Enter "result" object

        while (in.hasNext()) {
            // Consume (and ignore) asset pair name
            in.nextName();

            // We're only expecting one result entry. If there are any more,
            // these are ignored.
            if (result != null) {
                in.skipValue();
                continue;
            }

            // Now read the data
            result = new Depth();
            in.beginObject();

            while (in.hasNext()) {
                switch (in.nextName()) {
                    case "asks":
                        ArrayList<Depth.Entry> asks = new ArrayList<>();
                        in.beginArray();

                        while (in.hasNext()) {
                            Depth.Entry entry = new Depth.Entry();
                            in.beginArray();
                            entry.price = in.nextString();
                            entry.volume = in.nextString();
                            entry.timestamp = in.nextLong();
                            in.endArray();
                            asks.add(entry);
                        }

                        in.endArray();
                        result.asks = new Depth.Entry[asks.size()];
                        asks.toArray(result.asks);
                        break;

                    case "bids":
                        ArrayList<Depth.Entry> bids = new ArrayList<>();
                        in.beginArray();

                        while (in.hasNext()) {
                            Depth.Entry entry = new Depth.Entry();
                            in.beginArray();
                            entry.price = in.nextString();
                            entry.volume = in.nextString();
                            entry.timestamp = in.nextLong();
                            in.endArray();
                            bids.add(entry);
                        }

                        in.endArray();
                        result.bids = new Depth.Entry[bids.size()];
                        bids.toArray(result.bids);
                        break;

                    default:
                        in.skipValue();
                        break;
                }
            }

            in.endObject();
        }

        in.endObject();
        return result;
    }

}
