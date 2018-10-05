package com.danykun.lokker.lib

import com.danykun.lokker.lib.error.ErrorListener
import com.danykun.lokker.lib.error.NoOpErrorListener
import com.danykun.lokker.lib.executor.Executor

/**
 * Reference to the request of an image loading with Lokker
 */
interface LokkerImageRequest {

    /**
     * Cancel the [LokkerImageRequest]
     */
    fun cancel()

    /**
     * Allow to configure the [LokkerImageRequest]
     */
    class Builder internal constructor(
            private val lokker: Lokker,
            private val executor: Executor,
            private val imageView: ImageView) {

        /**
         * Callback when an error occurs during
         */
        var errorListener: ErrorListener? = NoOpErrorListener()
        /**
         * Called when the image request completes: thqt is on success, on error or if cqncelled
         */
        var onCompleted: () -> Unit = {}

        /**
         * Start the request to load an image from a rmeote url to an [ImageView]
         * @param url  image remote url
         * @param config config block to specify some behaviors for the request
         */
        fun setImageFromUrl(url: String,
                            config: Builder.() -> Unit): LokkerImageRequest {
            apply(config)
            executor.config.apply {
                errorListener = { throwable -> this@Builder.errorListener?.onError(imageView, throwable) }
                onCompleted = this@Builder.onCompleted
            }
            return lokker.loadImageInto(executor, imageView, url)
        }
    }
}
