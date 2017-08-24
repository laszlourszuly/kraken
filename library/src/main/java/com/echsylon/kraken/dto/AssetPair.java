package com.echsylon.kraken.dto;

import com.google.gson.annotations.SerializedName;

/**
 * For technical details on the API see the online documentation:
 * https://www.kraken.com/help/api
 */
@SuppressWarnings("WeakerAccess")
public final class AssetPair {

    public static final class FeeSchedule {
        public Integer volume;
        public Double feePercent;
    }


    @SerializedName("altname")
    public String alternativeName;

    @SerializedName("aclass_base")
    public String baseAssetClass;

    @SerializedName("base")
    public String base;

    @SerializedName("aclass_quote")
    public String quoteAssetClass;

    @SerializedName("quote")
    public String quote;

    @SerializedName("lot")
    public String lot;

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

    @SerializedName("fees")
    public FeeSchedule[] fees;

    @SerializedName("fees_maker")
    public FeeSchedule[] makerFees;

    @SerializedName("fee_volume_currency")
    public String volumeCurrencyFee;

    @SerializedName("margin_call")
    public Integer callMargin;

    @SerializedName("margin_stop")
    public Integer stopMargin;

}
