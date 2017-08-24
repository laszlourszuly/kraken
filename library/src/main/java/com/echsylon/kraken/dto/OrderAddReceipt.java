package com.echsylon.kraken.dto;

import com.google.gson.annotations.SerializedName;

/**
 * For technical details on the API see the online documentation:
 * https://www.kraken.com/help/api
 */
@SuppressWarnings("WeakerAccess")
public final class OrderAddReceipt {

    public static final class Description {

        @SerializedName("order")
        public String order;

        @SerializedName("close")
        public String close;

    }


    @SerializedName("descr")
    public Description description;

    @SerializedName("txid")
    public String[] transactionId;

}
