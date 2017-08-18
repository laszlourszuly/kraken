package com.echsylon.kraken;

import com.google.gson.annotations.SerializedName;

/**
 * INTERNAL USE ONLY!
 * <p>
 * This class describes a response to a Kraken request. Note that Kraken doesn't
 * necessarily respond with HTTP error status codes if something goes wrong.
 * <p>
 * For technical details on the API see the online documentation:
 * https://www.kraken.com/help/api
 */
class KrakenResponse<T> {

    @SerializedName("error")
    String[] error;

    @SerializedName("result")
    T result;

}
