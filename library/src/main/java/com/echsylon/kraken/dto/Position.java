package com.echsylon.kraken.dto;

/**
 * For technical details on the API see the online documentation:
 * https://www.kraken.com/help/api
 */
@SuppressWarnings("WeakerAccess")
public final class Position {
    public String txId;
    public String orderTxId;
    public String pair;
    public Double time;
    public String type;
    public String orderType;
    public String cost;
    public String fee;
    public String vol;
    public String volClosed;
    public String margin;
    public String value;
    public String net;
    public String misc;
    public String oflags;
}
