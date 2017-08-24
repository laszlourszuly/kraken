package com.echsylon.kraken.dto;

import com.google.gson.annotations.SerializedName;

/**
 * For technical details on the API see the online documentation:
 * https://www.kraken.com/help/api
 */
@SuppressWarnings("WeakerAccess")
public final class Ticker {

    public static final class Price {
        public String price;
        public String lotVolume;
        public String wholeLotVolume;
    }


    public static final class Trade {
        public String price;
        public String lotVolume;
    }


    public static final class Span {
        public String today;
        public String last24Hours;
    }


    @SerializedName("a")
    public Price ask;

    @SerializedName("b")
    public Price bid;

    @SerializedName("c")
    public Trade lastClosedTrade;

    @SerializedName("v")
    public Span volume;

    @SerializedName("p")
    public Span volumeWeightedAveragePrice;

    @SerializedName("t")
    public Span numberOfTrades;

    @SerializedName("l")
    public Span low;

    @SerializedName("h")
    public Span high;

    @SerializedName("o")
    public String openingPriceToday;

}

