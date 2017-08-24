package com.echsylon.kraken.dto;

import com.google.gson.annotations.SerializedName;

/**
 * For technical details on the API see the online documentation:
 * https://www.kraken.com/help/api
 */
@SuppressWarnings("WeakerAccess")
public final class TradeBalance {

    @SerializedName("eb")
    public String equivalentBalance;

    @SerializedName("tb")
    public String tradeBalance;

    @SerializedName("m")
    public String margin;

    @SerializedName("n")
    public String net;

    @SerializedName("c")
    public String cost;

    @SerializedName("v")
    public String floatingValuation;

    @SerializedName("e")
    public String equity;

    @SerializedName("mf")
    public String freeMargin;

    @SerializedName("ml")
    public String marginLevel;

}
