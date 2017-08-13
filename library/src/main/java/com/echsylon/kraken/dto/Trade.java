package com.echsylon.kraken.dto;

/**
 * For technical details on the API see the online documentation:
 * https://www.kraken.com/help/api
 */
@SuppressWarnings("WeakerAccess")
public final class Trade {
    public String price;
    public String volume;
    public Double time;
    public String buyOrSell;
    public String marketOrLimit;
    public String misc;
}
