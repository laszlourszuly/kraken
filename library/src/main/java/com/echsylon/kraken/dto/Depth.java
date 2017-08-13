package com.echsylon.kraken.dto;

/**
 * For technical details on the API see the online documentation:
 * https://www.kraken.com/help/api
 */
@SuppressWarnings("WeakerAccess")
public final class Depth {

    public static final class Entry {
        public String price;
        public String volume;
        public Long timestamp;
    }

    public Entry[] asks;
    public Entry[] bids;
}
