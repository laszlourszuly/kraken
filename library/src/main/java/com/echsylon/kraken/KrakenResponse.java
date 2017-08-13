package com.echsylon.kraken;

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

    /**
     * The error messages from Kraken, or null if everything went well.
     */
    String[] error;

    /**
     * The result data structure, or null if something went wrong.
     */
    T result;

}
