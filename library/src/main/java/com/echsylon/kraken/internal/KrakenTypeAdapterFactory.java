package com.echsylon.kraken.internal;

import com.echsylon.kraken.Dictionary;
import com.echsylon.kraken.dto.AssetPair;
import com.echsylon.kraken.dto.Depth;
import com.echsylon.kraken.dto.Ohlc;
import com.echsylon.kraken.dto.Spread;
import com.echsylon.kraken.dto.Ticker;
import com.echsylon.kraken.dto.Trade;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * This class knows how to create custom JSON type adapters. These type adapters
 * are used for those cases where the Kraken API returns an array of data as a
 * tuple that's more suitable to represent as a POJO.
 */
public final class KrakenTypeAdapterFactory implements TypeAdapterFactory {

    @Override
    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        switch (typeToken.getRawType().getName()) {
            case "com.echsylon.kraken.Dictionary":
                // The dictionary is always expected to be parametrized
                ParameterizedType type = (ParameterizedType) typeToken.getType();
                Type[] parameterTypes = type.getActualTypeArguments();
                TypeToken typeTypeToken = TypeToken.get(parameterTypes[0]);
                TypeAdapter<?> valueAdapter = gson.getAdapter(typeTypeToken);
                return (TypeAdapter<T>) new DictionaryAdapter(valueAdapter);

            case "com.echsylon.kraken.dto.AssetPair$FeeSchedule":
                return (TypeAdapter<T>) new FeeScheduleAdapter();

            case "com.echsylon.kraken.dto.Depth$Offer":
                return (TypeAdapter<T>) new OfferAdapter();

            case "com.echsylon.kraken.dto.Ohlc":
                return (TypeAdapter<T>) new OhlcAdapter();

            case "com.echsylon.kraken.dto.Ticker$Price":
                return (TypeAdapter<T>) new TickerPriceAdapter();

            case "com.echsylon.kraken.dto.Ticker$Span":
                return (TypeAdapter<T>) new TickerSpanAdapter();

            case "com.echsylon.kraken.dto.Ticker$Trade":
                return (TypeAdapter<T>) new TickerTradeAdapter();

            case "com.echsylon.kraken.dto.Trade":
                return (TypeAdapter<T>) new TradeAdapter();

            case "com.echsylon.kraken.dto.Spread":
                return (TypeAdapter<T>) new SpreadAdapter();

            default:
                return null;
        }
    }


    // START: Custom Type Adapters

    private abstract class ReadOnlyTypeAdapter<T> extends TypeAdapter<T> {
        @Override
        public void write(JsonWriter out, T value) throws IOException {
            // Instances of this class are not expected to write JSON.
        }
    }


    private final class DictionaryAdapter<V> extends ReadOnlyTypeAdapter<Dictionary<V>> {
        private final TypeAdapter<V> valueAdapter;

        private DictionaryAdapter(final TypeAdapter<V> valueAdapter) {
            this.valueAdapter = valueAdapter;
        }

        @Override
        public Dictionary<V> read(JsonReader in) throws IOException {
            Dictionary<V> result = new Dictionary<>();
            String attributeName;
            in.beginObject();

            while (in.hasNext()) {
                attributeName = in.nextName();

                switch (attributeName) {
                    case "last":
                        result.last = in.nextString();
                        break;

                    case "count":
                        result.count = in.nextInt();
                        break;

                    case "open":    // Orders
                    case "closed":
                    case "trades":  // TradesHistory
                    case "ledger":  // Ledger
                        if (valueAdapter == null) {
                            in.skipValue();
                        } else {
                            in.beginObject();
                            while (in.hasNext())
                                result.put(in.nextName(), valueAdapter.read(in));
                            in.endObject();
                        }
                        break;

                    default:
                        if (valueAdapter == null)
                            in.skipValue();
                        else
                            result.put(attributeName, valueAdapter.read(in));
                        break;
                }
            }

            in.endObject();
            return result;
        }
    }


    private final class FeeScheduleAdapter extends ReadOnlyTypeAdapter<AssetPair.FeeSchedule> {
        @Override
        public AssetPair.FeeSchedule read(JsonReader in) throws IOException {
            in.beginArray();
            AssetPair.FeeSchedule result = new AssetPair.FeeSchedule();
            result.volume = in.nextInt();
            result.feePercent = in.nextDouble();
            in.endArray();
            return result;
        }
    }


    private final class OfferAdapter extends ReadOnlyTypeAdapter<Depth.Offer> {
        @Override
        public Depth.Offer read(JsonReader in) throws IOException {
            in.beginArray();
            Depth.Offer result = new Depth.Offer();
            result.price = in.nextString();
            result.volume = in.nextString();
            result.timestamp = in.nextLong();
            in.endArray();
            return result;
        }
    }


    private final class OhlcAdapter extends ReadOnlyTypeAdapter<Ohlc> {
        @Override
        public Ohlc read(JsonReader in) throws IOException {
            Ohlc result = new Ohlc();
            in.beginArray();
            result.time = in.nextLong();
            result.open = in.nextString();
            result.high = in.nextString();
            result.low = in.nextString();
            result.close = in.nextString();
            result.volumeWeightedAveragePrice = in.nextString();
            result.volume = in.nextString();
            result.count = in.nextInt();
            in.endArray();
            return result;
        }
    }


    private final class TickerPriceAdapter extends ReadOnlyTypeAdapter<Ticker.Price> {
        @Override
        public Ticker.Price read(JsonReader in) throws IOException {
            in.beginArray();
            Ticker.Price result = new Ticker.Price();
            result.price = in.nextString();
            result.wholeLotVolume = in.nextString();
            result.lotVolume = in.nextString();
            in.endArray();
            return result;
        }
    }


    private final class TickerSpanAdapter extends ReadOnlyTypeAdapter<Ticker.Span> {
        @Override
        public Ticker.Span read(JsonReader in) throws IOException {
            in.beginArray();
            Ticker.Span result = new Ticker.Span();
            result.today = in.nextString();
            result.last24Hours = in.nextString();
            in.endArray();
            return result;
        }
    }


    private final class TickerTradeAdapter extends ReadOnlyTypeAdapter<Ticker.Trade> {
        @Override
        public Ticker.Trade read(JsonReader in) throws IOException {
            in.beginArray();
            Ticker.Trade result = new Ticker.Trade();
            result.price = in.nextString();
            result.lotVolume = in.nextString();
            in.endArray();
            return result;
        }
    }


    private final class TradeAdapter extends ReadOnlyTypeAdapter<Trade> {
        @Override
        public Trade read(JsonReader in) throws IOException {
            in.beginArray();
            Trade result = new Trade();
            result.price = in.nextString();
            result.volume = in.nextString();
            result.time = in.nextDouble();
            result.buyOrSell = in.nextString();
            result.marketOrLimit = in.nextString();
            result.misc = in.nextString();
            in.endArray();
            return result;
        }
    }


    private final class SpreadAdapter extends ReadOnlyTypeAdapter<Spread> {
        @Override
        public Spread read(JsonReader in) throws IOException {
            in.beginArray();
            Spread result = new Spread();
            result.timestamp = in.nextLong();
            result.bid = in.nextString();
            result.ask = in.nextString();
            in.endArray();
            return result;
        }
    }

}

