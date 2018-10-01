package com.danykun.lokker.lib

import android.widget.ImageView
import com.danykun.lokker.lib.error.ErrorListener
import com.danykun.lokker.lib.error.NoOpErrorListener
import com.danykun.lokker.lib.executor.Executor

interface LokkerImageRequest {

    fun cancel()

    class Builder internal constructor(
        private val lokker: Lokker,
        private val executor: Executor,
        private val imageView: ImageView) {

        var errorListener: ErrorListener? = NoOpErrorListener()
        var onCompleted: () -> Unit = {}

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
