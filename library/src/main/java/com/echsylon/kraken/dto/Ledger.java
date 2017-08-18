package com.echsylon.kraken.dto;

import com.google.gson.annotations.SerializedName;

/**
 * For technical details on the API see the online documentation:
 * https://www.kraken.com/help/api
 */
@SuppressWarnings("WeakerAccess")
public final class Ledger {

    @SerializedName("refid")
    public String refId;

    @SerializedName("time")
    public Double time;

    @SerializedName("type")
    public String type;

    @SerializedName("aclass")
    public String aClass;

    @SerializedName("asset")
    public String asset;

    @SerializedName("amount")
    public String amount;

    @SerializedName("fee")
    public String fee;

    @SerializedName("balance")
    public String balance;

}
