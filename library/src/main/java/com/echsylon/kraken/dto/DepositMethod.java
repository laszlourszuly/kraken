package com.echsylon.kraken.dto;

import com.google.gson.annotations.SerializedName;

/**
 * For technical details on the API see the online documentation:
 * https://www.kraken.com/help/api
 */
@SuppressWarnings("WeakerAccess")
public class DepositMethod {

    @SerializedName("method")
    public String method;

    @SerializedName("limit")
    public String limit;

    @SerializedName("fee")
    public String fee;

    @SerializedName("gen-address")
    public Boolean hasGeneratedAddress;

    @SerializedName("address-setup-fee")
    public String addressSetupFee;

}
