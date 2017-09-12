package com.echsylon.kraken.dto;

import com.google.gson.annotations.SerializedName;

/**
 * For technical details on the API see the online documentation:
 * https://www.kraken.com/help/api
 */
@SuppressWarnings("WeakerAccess")
public final class OrderCancelReceipt {

    @SerializedName("count")
    public Integer count;

    @SerializedName("pending")
    public Boolean pending;

}
