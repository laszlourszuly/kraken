package com.echsylon.kraken.dto;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * For technical details on the API see the online documentation:
 * https://www.kraken.com/help/api
 */
@SuppressWarnings("WeakerAccess")
public final class TradeVolume {

    public static final class FeeInfo {
        public String fee;
        @SerializedName("minfee")
        public String minFee;
        @SerializedName("maxfee")
        public String maxFee;
        @SerializedName("nextfee")
        public String nextFee;
        @SerializedName("nextvolume")
        public String nextVolume;
        @SerializedName("tiervolume")
        public String tierVolume;
    }


    public String currency;
    public String volume;
    public Map<String, FeeInfo> fees;
    @SerializedName("fees_maker")
    public Map<String, FeeInfo> feesMaker;
}
