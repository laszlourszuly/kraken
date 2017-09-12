package com.echsylon.kraken.dto;

import com.google.gson.annotations.SerializedName;

/**
 * For technical details on the API see the online documentation:
 * https://www.kraken.com/help/api
 */
@SuppressWarnings("WeakerAccess")
public class WithdrawStatus {

    @SerializedName("method")
    public String method;

    @SerializedName("aclass")
    public String assetClass;

    @SerializedName("asset")
    public String asset;

    @SerializedName("refid")
    public String referenceId;

    @SerializedName("txid")
    public String transactionId;

    @SerializedName("info")
    public String transactionInfo;

    @SerializedName("amount")
    public String depositedAmount;

    @SerializedName("fee")
    public String fee;

    @SerializedName("time")
    public Long timestamp;

    @SerializedName("status")
    public String status;

    @SerializedName("status-prop")
    public String properties;

}
