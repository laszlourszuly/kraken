package com.echsylon.kraken.dto;

import com.google.gson.annotations.SerializedName;

/**
 * For technical details on the API see the online documentation:
 * https://www.kraken.com/help/api
 */
@SuppressWarnings("WeakerAccess")
public final class OrderReceipt {

    public static final class Description {
        public String order;
        public String close;
    }


    public Description descr;
    @SerializedName("txid")
    public String[] txId;

}
