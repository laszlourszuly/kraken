package com.echsylon.kraken.dto;

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


    public Price a;
    public Price b;
    public Trade c;
    public Span v;
    public Span p;
    public Span t;
    public Span l;
    public Span h;
    public String o;
}

