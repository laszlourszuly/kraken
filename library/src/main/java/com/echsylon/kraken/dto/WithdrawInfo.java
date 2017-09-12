package com.echsylon.kraken.dto;

import com.google.gson.annotations.SerializedName;

/**
 * For technical details on the API see the online documentation:
 * https://www.kraken.com/help/api
 */
@SuppressWarnings("WeakerAccess")
public class WithdrawInfo {

    @SerializedName("method")
    public String method;

    @SerializedName("limit")
    public String limit;

    @SerializedName("fee")
    public String fee;

    @SerializedName("amount")
    public String netTotal;

}
