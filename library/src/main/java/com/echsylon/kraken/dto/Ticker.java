package com.echsylon.kraken.dto;

/**
 * For technical details on the API see the online documentation:
 * https://www.kraken.com/help/api
 */
public final class Ticker {

    @SuppressWarnings("WeakerAccess")
    public static final class Price<T> {
        public T price;
        public T lotVolume;
        public T wholeLotVolume;
    }

    @SuppressWarnings("WeakerAccess")
    public static final class Span<T> {
        public T today;
        public T last24Hours;
    }


    public Price<String> a;
    public Price<String> b;
    public Price<String> c;
    public Span<String> v;
    public Span<String> p;
    public Span<Integer> t;
    public Span<String> l;
    public Span<String> h;
    public String o;
}

