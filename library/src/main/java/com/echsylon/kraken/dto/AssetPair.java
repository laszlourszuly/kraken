package com.echsylon.kraken.dto;

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

    public String altName;
    public String aClassBase;
    public String base;
    public String aClassQuote;
    public String quote;
    public Integer lot;
    public Integer pairDecimals;
    public Integer lotDecimals;
    public Integer lotMultiplier;
    public Integer[] leverageBuy;
    public Integer[] leverageSell;
    public FeeSchedule[] fees;
    public FeeSchedule[] feesMaker;
    public String feeVolumeCurrency;
    public Integer marginCall;
    public Integer marginStop;
}
