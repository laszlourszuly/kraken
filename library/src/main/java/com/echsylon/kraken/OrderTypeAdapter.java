package com.echsylon.kraken;

import com.echsylon.kraken.dto.Order;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class knows how to parse Kraken order JSON into a corresponding Java
 * object.
 * <p>
 * For technical details on the API see the online documentation:
 * https://www.kraken.com/help/api
 */
final class OrderTypeAdapter extends TypeAdapter<KrakenList<Order>> {

    @Override
    public void write(JsonWriter out, KrakenList<Order> value) throws IOException {
        // We're not supposed to write order jSON.
    }

    @Override
    public KrakenList<Order> read(JsonReader in) throws IOException {
        KrakenList<Order> result = new KrakenList<>();
        in.beginObject(); // Enter "result" node

        while (in.hasNext()) {
            switch (in.nextName()) {
                case "count":
                    result.count = in.nextInt();
                    break;

                case "open": // intentional fall-through
                case "closed":
                    in.beginObject();
                    readOrder(in, result);
                    in.endObject();
                    break;

                default:
                    // Specifically queried orders aren't wrapped like the
                    // "open" and "closed" orders are.
                    readOrder(in, result);
                    break;
            }
        }
        in.endObject();
        return result;
    }

    // Parse the actual order info
    private void readOrder(JsonReader in, KrakenList<Order> orders) throws IOException {
        while (in.hasNext()) {
            Order data = new Order();
            data.txId = in.nextName();
            in.beginObject();

            while (in.hasNext()) {
                switch (in.nextName()) {
                    case "refid":
                        data.refId = in.nextString();
                        break;
                    case "userref":
                        data.userRef = in.nextString();
                        break;
                    case "status":
                        data.status = in.nextString();
                        break;
                    case "opentm":
                        data.openTimestamp = in.nextDouble();
                        break;
                    case "starttm":
                        data.startTimestamp = in.nextDouble();
                        break;
                    case "expiretm":
                        data.expireTimestamp = in.nextDouble();
                        break;
                    case "descr":
                        data.description = new Order.Description();
                        in.beginObject();
                        while (in.hasNext()) {
                            switch (in.nextName()) {
                                case "pair":
                                    data.description.pair = in.nextString();
                                    break;
                                case "type":
                                    data.description.type = in.nextString();
                                    break;
                                case "ordertype":
                                    data.description.orderType = in.nextString();
                                    break;
                                case "price":
                                    data.description.price = in.nextString();
                                    break;
                                case "price2":
                                    data.description.price2 = in.nextString();
                                    break;
                                case "leverage":
                                    data.description.leverage = in.nextString();
                                    break;
                                case "order":
                                    data.description.order = in.nextString();
                                    break;
                                case "close":
                                    data.description.close = in.nextString();
                                    break;
                                default:
                                    in.skipValue();
                                    break;
                            }
                        }
                        in.endObject();
                        break;
                    case "vol":
                        data.volume = in.nextString();
                        break;
                    case "vol_exec":
                        data.executedVolume = in.nextString();
                        break;
                    case "cost":
                        data.cost = in.nextString();
                        break;
                    case "fee":
                        data.fee = in.nextString();
                        break;
                    case "price":
                        data.price = in.nextString();
                        break;
                    case "stopprice":
                        data.stopPrice = in.nextString();
                        break;
                    case "limitprice":
                        data.limitPrice = in.nextString();
                        break;
                    case "misc":
                        data.misc = in.nextString();
                        break;
                    case "oflags":
                        data.oflags = in.nextString();
                        break;
                    case "trades":
                        List<String> trades = new ArrayList<>();
                        in.beginArray();
                        while (in.hasNext())
                            trades.add(in.nextString());
                        data.trades = new String[trades.size()];
                        trades.toArray(data.trades);
                        in.endArray();
                        break;
                    default:
                        in.skipValue();
                        break;
                }
            }

            in.endObject();
            orders.add(data);
        }
    }
}
