package com.echsylon.kraken;

import java.util.HashMap;

/**
 * This class knows how to represent a dictionary of Kraken domain objects
 * that holds extra meta information about the data.
 * <p>
 * For technical details on the API see the online documentation:
 * https://www.kraken.com/help/api
 */
@SuppressWarnings("WeakerAccess")
public class Dictionary<V> extends HashMap<String, V> {

    /**
     * If applicable; holds the reference to the last item included in the
     * list, otherwise null.
     */
    public String last;

    /**
     * If applicable; holds the number of items matching a search criteria
     * (not necessarily the amount of items in the list), otherwise null.
     */
    public Integer count;

}
