package com.echsylon.kraken;

import java.util.HashMap;

/**
 * This class knows how to represent a dictionary of Kraken domain objects
 * that holds extra meta information about the data.
 * <p>
 * For technical details on the API see the online documentation:
 * https://www.kraken.com/help/api
 */
public class KrakenMap<K, V> extends HashMap<K, V> {

    /**
     * If applicable; holds the reference to the last item included in the
     * list, otherwise null.
     */
    public String last;

}
