package com.echsylon.kraken.dto;

import com.google.gson.annotations.SerializedName;

/**
 * For technical details on the API see the online documentation:
 * https://www.kraken.com/help/api
 */
public final class Time {

    @SerializedName("unixtime")
    public Long unixtime;

    @SerializedName("rfc1123")
    public String rfc1123;

}
