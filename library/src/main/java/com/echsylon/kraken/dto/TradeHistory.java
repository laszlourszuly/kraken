package com.echsylon.kraken.dto;

/**
 * For technical details on the API see the online documentation:
 * https://www.kraken.com/help/api
 */
@SuppressWarnings("WeakerAccess")
public final class TradeHistory {
    public String tradeTxId;
    public String orderTxId;
    public String positionStatus;
    public String pair;
    public Double time;
    public String type;
    public String orderType;
    public String price;
    public String cPrice;
    public String cost;
    public String cCost;
    public String fee;
    public String cFee;
    public String volume;
    public String cVolume;
    public String margin;
    public String cMargin;
    public String misc;
    public String net;
    public String[] trades;
}
