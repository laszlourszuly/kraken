package com.echsylon.kraken.dto;

/**
 * For technical details on the API see the online documentation:
 * https://www.kraken.com/help/api
 */
@SuppressWarnings("WeakerAccess")
public final class Ledger {
    public String id;
    public String refId;
    public Double time;
    public String type;
    public String aClass;
    public String asset;
    public String amount;
    public String fee;
    public String balance;
}
