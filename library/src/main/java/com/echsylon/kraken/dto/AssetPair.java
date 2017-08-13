package com.echsylon.kraken.dto;

import com.google.gson.annotations.SerializedName;

/**
 * For technical details on the API see the online documentation:
 * https://www.kraken.com/help/api
 */
public final class AssetPair {
    public String altname;
    @SerializedName("aclass_base")
    public String aclassBase;
    public String base;
    @SerializedName("aclass_quote")
    public String aclassQuote;
    public String quote;
    public Integer lot;
    @SerializedName("pair_decimals")
    public Integer pairDecimals;
    @SerializedName("lot_decimals")
    public Integer lotDecimals;
    @SerializedName("lot_multiplier")
    public Integer lotMultiplier;
    @SerializedName("leverage_buy")
    public Integer[] leverageBuy;
    @SerializedName("leverage_sell")
    public Integer[] leverageSell;
    public Float[] fees;
    @SerializedName("fees_maker")
    public Float[] feesMaker;
    @SerializedName("fee_volume_currency")
    public String feeVolumeCurrency;
    @SerializedName("margin_call")
    public Integer marginCall;
    @SerializedName("margin_stop")
    public Integer marginStop;
}
