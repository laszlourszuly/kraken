package com.echsylon.kraken.dto;

import com.google.gson.annotations.SerializedName;

/**
 * For technical details on the API see the online documentation:
 * https://www.kraken.com/help/api
 */
@SuppressWarnings("WeakerAccess")
public class DepositAddress {

    @SerializedName("address")
    public String address;

    @SerializedName("expiretm")
    public String expiryTime;

    @SerializedName("new")
    public Boolean isNew;

}
