package com.echsylon.kraken.dto;

import com.google.gson.annotations.SerializedName;

/**
 * For technical details on the API see the online documentation:
 * https://www.kraken.com/help/api
 */
@SuppressWarnings("WeakerAccess")
public final class Position {

    @SerializedName("ordertxid")
    public String orderTxId;

    @SerializedName("pair")
    public String pair;

    @SerializedName("time")
    public Double time;

    @SerializedName("type")
    public String type;

    @SerializedName("ordertype")
    public String orderType;

    @SerializedName("cost")
    public String cost;

    @SerializedName("fee")
    public String fee;

    @SerializedName("vol")
    public String vol;

    @SerializedName("vol_closed")
    public String volClosed;

    @SerializedName("margin")
    public String margin;

    @SerializedName("value")
    public String value;

    @SerializedName("net")
    public String net;

    @SerializedName("misc")
    public String misc;

    @SerializedName("oflags")
    public String oflags;

}
