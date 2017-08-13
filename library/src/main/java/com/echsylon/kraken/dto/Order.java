package com.echsylon.kraken.dto;

/**
 * For technical details on the API see the online documentation:
 * https://www.kraken.com/help/api
 */
@SuppressWarnings("WeakerAccess")
public final class Order {

    public static final class Description {
        public String pair;
        public String type;
        public String orderType;
        public String price;
        public String price2;
        public String leverage;
        public String order;
        public String close;
    }


    public String txId;
    public String refId;
    public String userRef;
    public String status;
    public Double openTimestamp;
    public Double startTimestamp;
    public Double expireTimestamp;
    public Description description;
    public String volume;
    public String executedVolume;
    public String cost;
    public String fee;
    public String price;
    public String stopPrice;
    public String limitPrice;
    public String misc;
    public String oflags;
    public String[] trades;
}
