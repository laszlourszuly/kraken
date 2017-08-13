package com.echsylon.kraken;

import com.echsylon.kraken.dto.TradeHistory;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * This class knows how to parse Kraken trade history JSON into a corresponding
 * Java object.
 * <p>
 * For technical details on the API see the online documentation:
 * https://www.kraken.com/help/api
 */
final class TradeHistoryTypeAdapter extends TypeAdapter<KrakenList<TradeHistory>> {

    @Override
    public void write(JsonWriter out, KrakenList<TradeHistory> value) throws IOException {
        // We're not supposed to write trade JSON.
    }

    @Override
    public KrakenList<TradeHistory> read(JsonReader in) throws IOException {
        KrakenList<TradeHistory> result = new KrakenList<>();
        in.beginObject(); // Enter "result"

        while (in.hasNext()) {
            switch (in.nextName()) {
                case "count":
                    result.count = in.nextInt();
                    break;

                case "trades":
                    in.beginObject();
                    readTrade(in, result);
                    in.endObject();
                    break;

                default:
                    // Specifically queried trades aren't wrapped like the
                    // "history" orders are.
                    in.skipValue();
                    break;
            }
        }

        in.endObject(); // Leave "result"
        return result;
    }

    // Parse the actual trade info
    private void readTrade(JsonReader in, KrakenList<TradeHistory> result) throws IOException {
        while (in.hasNext()) {
            TradeHistory history = new TradeHistory();
            history.tradeTxId = in.nextName();
            in.beginObject();   // Enter trade

            while (in.hasNext()) {
                switch (in.nextName()) {
                    case "ordertxid":
                        history.orderTxId = in.nextString();
                        break;
                    case "posstatus":
                        history.positionStatus = in.nextString();
                        break;
                    case "pair":
                        history.pair = in.nextString();
                        break;
                    case "time":
                        history.time = in.nextDouble();
                        break;
                    case "type":
                        history.type = in.nextString();
                        break;
                    case "ordertype":
                        history.orderType = in.nextString();
                        break;
                    case "price":
                        history.price = in.nextString();
                        break;
                    case "cprice":
                        history.cPrice = in.nextString();
                        break;
                    case "cost":
                        history.cost = in.nextString();
                        break;
                    case "ccost":
                        history.cCost = in.nextString();
                        break;
                    case "fee":
                        history.fee = in.nextString();
                        break;
                    case "cfee":
                        history.cFee = in.nextString();
                        break;
                    case "vol":
                        history.volume = in.nextString();
                        break;
                    case "cvol":
                        history.cVolume = in.nextString();
                        break;
                    case "margin":
                        history.margin = in.nextString();
                        break;
                    case "cmargin":
                        history.cMargin = in.nextString();
                        break;
                    case "misc":
                        history.misc = in.nextString();
                        break;
                    case "net":
                        history.net = in.nextString();
                    case "trades":
                        List<String> trades = new ArrayList<>();
                        in.beginArray();
                        while (in.hasNext())
                            trades.add(in.nextString());
                        history.trades = new String[trades.size()];
                        trades.toArray(history.trades);
                        in.endArray();
                        break;
                    default:
                        in.skipValue();
                        break;
                }
            }

            in.endObject(); // Leave trade
            result.add(history);
        }
    }

}
