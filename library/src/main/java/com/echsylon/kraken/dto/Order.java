package com.echsylon.kraken.dto;

import com.google.gson.annotations.SerializedName;

/**
 * For technical details on the API see the online documentation:
 * https://www.kraken.com/help/api
 */
@SuppressWarnings("WeakerAccess")
public final class Order {

    public static final class Description {

        @SerializedName("pair")
        public String pair;

        @SerializedName("type")
        public String type;

        @SerializedName("ordertype")
        public String orderType;

        @SerializedName("price")
        public String price;

        @SerializedName("price2")
        public String secondaryPrice;

        @SerializedName("leverage")
        public String leverage;

        @SerializedName("order")
        public String order;

        @SerializedName("close")
        public String close;

    }


    @SerializedName("refid")
    public String referenceId;

    @SerializedName("userref")
    public String userReference;

    @SerializedName("status")
    public String status;

    @SerializedName("reason")
    public String reason;

    @SerializedName("opentm")
    public Double openTime;

    @SerializedName("closetm")
    public Double closeTime;

    @SerializedName("starttm")
    public Double startTime;

    @SerializedName("expiretm")
    public Double expireTime;

    @SerializedName("descr")
    public Description description;

    @SerializedName("vol")
    public String volume;

    @SerializedName("vol_exec")
    public String executedVolume;

    @SerializedName("cost")
    public String cost;

    @SerializedName("fee")
    public String fee;

    @SerializedName("price")
    public String price;

    @SerializedName("stopprice")
    public String stopPrice;

    @SerializedName("limitprice")
    public String limitPrice;

    @SerializedName("misc")
    public String misc;

    @SerializedName("oflags")
    public String orderFlags;

    @SerializedName("trades")
    public String[] trades;

}
