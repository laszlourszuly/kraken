package com.echsylon.kraken;

import com.echsylon.kraken.dto.Ticker;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class knows how to parse Kraken ticker JSON into a corresponding Java
 * object.
 * <p>
 * For technical details on the API see the online documentation:
 * https://www.kraken.com/help/api
 */
final class TickerTypeAdapter extends TypeAdapter<Map<String, Ticker>> {

    @Override
    public void write(JsonWriter out, Map<String, Ticker> value) throws IOException {
        // We're not supposed to write ticker JSON.
    }

    @Override
    public Map<String, Ticker> read(JsonReader in) throws IOException {
        Map<String, Ticker> result = new HashMap<>();
        in.beginObject();       // Enter "result" object

        while (in.hasNext()) {
            String key = in.nextName();
            Ticker data = new Ticker();
            in.beginObject();   // Enter asset pair object

            switch (in.nextName()) {
                case "a":
                    data.a = new Ticker.Price<>();
                    in.beginArray();
                    data.a.price = in.nextString();
                    data.a.wholeLotVolume = in.nextString();
                    data.a.lotVolume = in.nextString();
                    in.endArray();
                    break;

                case "b":
                    data.b = new Ticker.Price<>();
                    in.beginArray();
                    data.b.price = in.nextString();
                    data.b.wholeLotVolume = in.nextString();
                    data.b.lotVolume = in.nextString();
                    in.endArray();
                    break;

                case "c":
                    data.c = new Ticker.Price<>();
                    in.beginArray();
                    data.c.price = in.nextString();
                    data.c.lotVolume = in.nextString();
                    in.endArray();
                    break;

                case "v":
                    data.v = new Ticker.Span<>();
                    in.beginArray();
                    data.v.today = in.nextString();
                    data.v.last24Hours = in.nextString();
                    in.endArray();
                    break;

                case "p":
                    data.p = new Ticker.Span<>();
                    in.beginArray();
                    data.p.today = in.nextString();
                    data.p.last24Hours = in.nextString();
                    in.endArray();
                    break;

                case "t":
                    data.t = new Ticker.Span<>();
                    in.beginArray();
                    data.t.today = in.nextInt();
                    data.t.last24Hours = in.nextInt();
                    in.endArray();
                    break;

                case "h":
                    data.h = new Ticker.Span<>();
                    in.beginArray();
                    data.h.today = in.nextString();
                    data.h.last24Hours = in.nextString();
                    in.endArray();
                    break;

                case "l":
                    data.l = new Ticker.Span<>();
                    in.beginArray();
                    data.l.today = in.nextString();
                    data.l.last24Hours = in.nextString();
                    in.endArray();
                    break;

                case "o":
                    data.o = in.nextString();
                    break;

                default:
                    in.skipValue();
                    break;
            }

            in.endObject();
            result.put(key, data);
        }

        in.endObject();
        return result;
    }

}
