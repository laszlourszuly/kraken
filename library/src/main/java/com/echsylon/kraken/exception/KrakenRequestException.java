package com.echsylon.kraken.exception;

import static android.text.TextUtils.join;

/**
 * This exception represents a Kraken request error.
 */
public class KrakenRequestException extends RuntimeException {

    public KrakenRequestException(String[] errors) {
        super(join("\n", errors));
    }

}
