package com.echsylon.kraken.request;

/**
 * This interface describes the "done" signaling callback. This callback doesn't
 * deliver any result or error cause, it just signals that a request has been
 * finished. The use case is for callers that need to perform some operations
 * common to both the success and error branches, for example dismissing a
 * progress dialog or so.
 */
public interface FinishListener {

    /**
     * Signals that a request has been fully handled. No information on success
     * or failure is available from here.
     */
    void onFinish();

}
