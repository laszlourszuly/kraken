package com.echsylon.kraken.dto;

/**
 * For technical details on the API see the online documentation:
 * https://www.kraken.com/help/api
 */
@SuppressWarnings("WeakerAccess")
public final class Ohlc {

    public Long time;
    public String open;
    public String high;
    public String low;
    public String close;
    public String volumeWeightedAveragePrice;
    public String volume;
    public Integer count;

}

