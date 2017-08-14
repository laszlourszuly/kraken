package com.echsylon.kraken;

import com.echsylon.blocks.callback.Request;
import com.echsylon.blocks.network.OkHttpNetworkClient;

/**
 * This class knows how to prepare a request for client side caching. The super
 * API offers means of configuring client side soft and hard caching.
 * <p>
 * A soft cache means that the cache metrics provided by the caller will only be
 * applied if the server itself didn't provide any cache metrics.
 * <p>
 * Similarly a hard cache means that the caller provided cache metrics will
 * override any cache metrics provided by the server.
 * <p>
 * NOTE! Implementing classes MUST use the {@code okHttpNetworkClient}, provided
 * by the super class as a protected field, when implementing {@code enqueue()}
 * in order to make use of any cache configuration made by the caller.
 */
public abstract class KrakenRequestBuilder<T> extends OkHttpNetworkClient.CachedRequestBuilder<KrakenRequestBuilder<T>> {

    /**
     * Creates and enqueues the actual request.
     *
     * @return A request object to attach any callback implementations to.
     */
    public abstract Request<T> enqueue();

}
