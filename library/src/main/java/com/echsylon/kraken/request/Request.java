package com.echsylon.kraken.request;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RejectedExecutionException;

/**
 * This class is responsible for representing a request which is executed in the background. The
 * caller can attach three types of listeners to this request which will be called once a result
 * is produced:
 *
 * A {@link SuccessListener} will receive any successfully produced data. The caller is responsible
 * for providing a correctly typed listener.
 *
 * A {@link ErrorListener} will receive any errors the request mechanism can't recover from. The
 * error is delivered (not thrown) as {@code Throwable} callback argument.
 *
 * A {@link FinishListener} will act as an event channel which simply signals that the request has
 * finished. No information is provided through this callback, not even the state of the response.
 * This channel can be used to perform any state reset operations, like hiding progress bars, etc,
 * which aren't dependant on the success or failure state of a request.
 *
 * @param <T> The type of result object that is expected from the request. Any attached {@code
 *            SuccessListeners} must have the same type of generic definition.
 */
public class Request<T> extends FutureTask<T> {
    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(5);
    private final CallbackManager<T> callbackManager;


    public Request(Callable<T> callable) throws RejectedExecutionException, NullPointerException {
        super(callable);
        callbackManager = new CallbackManager<>();
        EXECUTOR.submit(this);
    }

    /**
     * Removes all previously added listeners and cancels the task. Note that
     * no callbacks are expected to be called if this request is cancelled.
     */
    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        callbackManager.terminate();
        return super.cancel(mayInterruptIfRunning);
    }

    /**
     * Used internally by the java concurrency framework. Don't call this method
     * on your own.
     */
    @Override
    protected void done() {
        super.done();
        try {
            callbackManager.deliverSuccessOnMainThread(get());
        } catch (InterruptedException | ExecutionException e) {
            callbackManager.deliverErrorOnMainThread(e.getCause());
        }
    }

    /**
     * Attaches a success listener to this request. Note that the listener will
     * only be called once and if the request produces an error, the listener
     * won't be called at all.
     *
     * @param listener The success listener.
     * @return This request object, allowing chaining of requests.
     */
    public Request<T> withSuccessListener(SuccessListener<T> listener) {
        callbackManager.addSuccessListener(listener);
        return this;
    }

    /**
     * Attaches an error listener to this request. Note that the listener will
     * only be called once and if the request doesn't produce an error, the
     * listener won't be called at all.
     *
     * @param listener The error listener.
     * @return This request object, allowing chaining of requests.
     */
    public Request<T> withErrorListener(ErrorListener listener) {
        callbackManager.addErrorListener(listener);
        return this;
    }

    /**
     * Attaches a finished state listener to this request. Note that the
     * listener will be called exactly once regardless if the produced result is
     * a success or a failure.
     *
     * @param listener The finish state listener.
     * @return This request object, allowing chaining of requests.
     */
    public Request<T> withFinishListener(FinishListener listener) {
        callbackManager.addFinishListener(listener);
        return this;
    }

}
