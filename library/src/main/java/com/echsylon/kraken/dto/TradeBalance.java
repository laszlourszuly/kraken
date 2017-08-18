package com.echsylon.kraken.dto;

import com.google.gson.annotations.SerializedName;

/**
 * For technical details on the API see the online documentation:
 * https://www.kraken.com/help/api
 */
@SuppressWarnings("WeakerAccess")
public final class TradeBalance {

    @SerializedName("eb")
    public String eb;

    @SerializedName("tb")
    public String tb;

    @SerializedName("m")
    public String m;

    @SerializedName("n")
    public String n;

    @SerializedName("c")
    public String c;

    @SerializedName("v")
    public String v;

    @SerializedName("e")
    public String e;

    @SerializedName("mf")
    public String mf;

    @SerializedName("ml")
    public String ml;

}
