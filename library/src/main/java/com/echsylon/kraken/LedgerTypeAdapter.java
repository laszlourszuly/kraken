package com.echsylon.kraken;

import com.echsylon.kraken.dto.Ledger;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;


/**
 * This class knows how to parse Kraken ledger JSON into a corresponding
 * Java object.
 * <p>
 * For technical details on the API see the online documentation:
 * https://www.kraken.com/help/api
 */
final class LedgerTypeAdapter extends TypeAdapter<KrakenList<Ledger>> {

    @Override
    public void write(JsonWriter out, KrakenList<Ledger> value) throws IOException {
        // We're not supposed to write position JSON.
    }

    @Override
    public KrakenList<Ledger> read(JsonReader in) throws IOException {
        KrakenList<Ledger> result = new KrakenList<>();
        in.beginObject(); // Enter "result"

        while (in.hasNext()) {
            switch (in.nextName()) {
                case "count":
                    result.count = in.nextInt();
                    break;

                case "ledger":
                    in.beginObject();
                    readLedger(in, result);
                    in.endObject();
                    break;

                default:
                    // The query request doesn't wrap the ledger data as the
                    // get info request does.
                    readLedger(in, result);
                    break;

            }

        }

        in.endObject(); // Leave "result"
        return result;

    }

    private void readLedger(JsonReader in, KrakenList<Ledger> result) throws IOException {
        while (in.hasNext()) {
            Ledger entry = new Ledger();
            entry.id = in.nextName();
            in.beginObject();

            while (in.hasNext()) {
                switch (in.nextName()) {
                    case "refid":
                        entry.refId = in.nextString();
                        break;
                    case "time":
                        entry.time = in.nextDouble();
                        break;
                    case "type":
                        entry.type = in.nextString();
                        break;
                    case "aclass":
                        entry.aClass = in.nextString();
                        break;
                    case "asset":
                        entry.asset = in.nextString();
                        break;
                    case "amount":
                        entry.amount = in.nextString();
                        break;
                    case "fee":
                        entry.fee = in.nextString();
                        break;
                    case "balance":
                        entry.balance = in.nextString();
                        break;
                    default:
                        in.skipValue();
                        break;
                }
            }

            in.endObject();
            result.add(entry);
        }
    }

}
