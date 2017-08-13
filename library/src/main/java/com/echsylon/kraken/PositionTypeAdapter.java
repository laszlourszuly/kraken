package com.echsylon.kraken;

import com.echsylon.kraken.dto.Position;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;


/**
 * This class knows how to parse Kraken position JSON into a corresponding
 * Java object.
 * <p>
 * For technical details on the API see the online documentation:
 * https://www.kraken.com/help/api
 */
final class PositionTypeAdapter extends TypeAdapter<KrakenList<Position>> {

    @Override
    public void write(JsonWriter out, KrakenList<Position> value) throws IOException {
        // We're not supposed to write position JSON.
    }

    @Override
    public KrakenList<Position> read(JsonReader in) throws IOException {
        KrakenList<Position> result = new KrakenList<>();
        in.beginObject(); // Enter "result"

        while (in.hasNext()) {
            switch (in.nextName()) {
                case "count":
                    result.count = in.nextInt();
                    break;

                case "position":
                    in.beginObject();

                    while (in.hasNext()) {
                        Position entry = new Position();
                        entry.txId = in.nextName();
                        in.beginObject();

                        while (in.hasNext()) {
                            switch (in.nextName()) {
                                case "ordertxid":
                                    entry.orderTxId = in.nextString();
                                    break;
                                case "pair":
                                    entry.pair = in.nextString();
                                    break;
                                case "time":
                                    entry.time = in.nextDouble();
                                    break;
                                case "type":
                                    entry.type = in.nextString();
                                    break;
                                case "ordertype":
                                    entry.orderType = in.nextString();
                                    break;
                                case "cost":
                                    entry.cost = in.nextString();
                                    break;
                                case "fee":
                                    entry.fee = in.nextString();
                                    break;
                                case "vol":
                                    entry.vol = in.nextString();
                                    break;
                                case "vol_closed":
                                    entry.volClosed = in.nextString();
                                    break;
                                case "margin":
                                    entry.margin = in.nextString();
                                    break;
                                case "value":
                                    entry.value = in.nextString();
                                    break;
                                case "net":
                                    entry.net = in.nextString();
                                    break;
                                case "misc":
                                    entry.misc = in.nextString();
                                    break;
                                case "oflags":
                                    entry.oflags = in.nextString();
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
                    break;

                default:
                    in.skipValue();
                    break;

            }

        }

        in.endObject(); // Leave "result"
        return result;

    }

}
