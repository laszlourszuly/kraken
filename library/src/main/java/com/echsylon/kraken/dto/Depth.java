package com.echsylon.kraken.dto;

import com.google.gson.annotations.SerializedName;

/**
 * For technical details on the API see the online documentation:
 * https://www.kraken.com/help/api
 */
@SuppressWarnings("WeakerAccess")
public final class Depth {

    public static final class Offer {
        public String price;
        public String volume;
        public Long timestamp;
    }


    @SerializedName("asks")
    public Offer[] asks;

    @SerializedName("bids")
    public Offer[] bids;

}
