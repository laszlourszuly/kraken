package com.echsylon.kraken;

import static android.text.TextUtils.join;

/**
 * This exception represents a Kraken request error.
 */
public class KrakenRequestException extends RuntimeException {

    public KrakenRequestException(String[] errors) {
        super(join("\n", errors));
    }

}
