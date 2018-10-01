package com.danykun.lokker.lib

import android.widget.ImageView
import com.danykun.lokker.lib.error.ErrorListener
import com.danykun.lokker.lib.error.NoOpErrorListener

interface LokkerImageRequest {

    fun cancel()

    interface Executor {
        var errorListener: ((Throwable) -> Unit)?
        var onCompleted: () -> Unit
        fun <T> execute(block: suspend () -> T, onUi: (T) -> Unit): LokkerImageRequest
    }

    class Builder internal constructor(
            private val lokker: Lokker,
            private val executor: Executor,
            private val imageView: ImageView) {

        fun setImageFromUrl(url: String,
                            errorListener: ErrorListener = NoOpErrorListener(),
                            onCompletedListener: (() -> Unit)? = null): LokkerImageRequest {
            executor.apply {
                this.errorListener = { error -> errorListener.onError(imageView, error) }
                onCompletedListener?.let { onCompleted = it }
            }
            return lokker.loadImageInto(executor, imageView, url)
        }
    }
}
