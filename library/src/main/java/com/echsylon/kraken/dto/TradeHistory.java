package com.echsylon.kraken.dto;

import com.google.gson.annotations.SerializedName;

/**
 * For technical details on the API see the online documentation:
 * https://www.kraken.com/help/api
 */
@SuppressWarnings("WeakerAccess")
public final class TradeHistory {

    @SerializedName("ordertxid")
    public String orderTxId;

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
    public String cPrice;

    @SerializedName("cost")
    public String cost;

    @SerializedName("ccost")
    public String cCost;

    @SerializedName("fee")
    public String fee;

    @SerializedName("cfee")
    public String cFee;

    @SerializedName("vol")
    public String vol;

    @SerializedName("cvol")
    public String cVol;

    @SerializedName("margin")
    public String margin;

    @SerializedName("cmargin")
    public String cMargin;

    @SerializedName("misc")
    public String misc;

    @SerializedName("net")
    public String net;

    @SerializedName("trades")
    public String[] trades;

}
