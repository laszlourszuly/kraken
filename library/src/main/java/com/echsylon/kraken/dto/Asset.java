package com.echsylon.kraken.dto;

import com.google.gson.annotations.SerializedName;

/**
 * For technical details on the API see the online documentation:
 * https://www.kraken.com/help/api
 */
@SuppressWarnings("WeakerAccess")
public final class Asset {

    @SerializedName("aclass")
    public String assetClass;

    @SerializedName("altname")
    public String alternativeName;

    @SerializedName("decimals")
    public Integer decimals;

    @SerializedName("display_decimals")
    public Integer displayDecimals;

}
