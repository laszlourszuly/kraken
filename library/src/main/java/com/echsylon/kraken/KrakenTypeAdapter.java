package com.echsylon.kraken;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * This class knows how to parse a Kraken response object with a custom response
 * type. The parsing of the actual success result is delegated to an injected
 * {@code TypeAdapter}, or skipped if none provided.
 * <p>
 * The main purpose of this class is to offload the Kraken data transport layer
 * parsing from any custom result type parsers.
 */
final class KrakenTypeAdapter<T> extends TypeAdapter<KrakenResponse<T>> {

    private final TypeAdapter<T> delegate;


    KrakenTypeAdapter(TypeAdapter<T> delegate) {
        this.delegate = delegate;
    }


    @Override
    public void write(JsonWriter out, KrakenResponse<T> value) throws IOException {
        // We're not supposed to write JSON.
    }

    @Override
    public KrakenResponse<T> read(JsonReader in) throws IOException {
        KrakenResponse<T> response = new KrakenResponse<>();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "error":
                    ArrayList<String> errors = new ArrayList<>();
                    in.beginArray();
                    while (in.hasNext())
                        errors.add(in.nextString());
                    in.endArray();
                    int count = errors.size();
                    if (count > 0) {
                        response.error = new String[count];
                        errors.toArray(response.error);
                    }
                    break;
                case "result":
                    if (delegate != null)
                        delegate.read(in);
                    else
                        in.skipValue();
                    break;
                default:
                    in.skipValue();
                    break;
            }
        }
        in.endObject();
        return response;
    }

}
