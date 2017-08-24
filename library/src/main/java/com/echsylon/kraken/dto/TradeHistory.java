package com.echsylon.kraken.dto;

import com.google.gson.annotations.SerializedName;

/**
 * For technical details on the API see the online documentation:
 * https://www.kraken.com/help/api
 */
@SuppressWarnings("WeakerAccess")
public final class TradeHistory {

    @SerializedName("ordertxid")
    public String orderTransactionId;

    @SerializedName("posstatus")
    public String positionStatus;

    @SerializedName("pair")
    public String pair;

    @SerializedName("time")
    public Double time;

    @SerializedName("type")
    public String type;

    @SerializedName("ordertype")
    public String orderType;

    @SerializedName("price")
    public String price;

    @SerializedName("cprice")
    public String closedPartPrice;

    @SerializedName("cost")
    public String cost;

    @SerializedName("ccost")
    public String closedPartCost;

    @SerializedName("fee")
    public String fee;

    @SerializedName("cfee")
    public String closedPartFee;

    @SerializedName("vol")
    public String volume;

    @SerializedName("cvol")
    public String closedPartVolume;

    @SerializedName("margin")
    public String margin;

    @SerializedName("cmargin")
    public String closedPartMargin;

    @SerializedName("misc")
    public String misc;

    @SerializedName("net")
    public String net;

    @SerializedName("trades")
    public String[] trades;

}
