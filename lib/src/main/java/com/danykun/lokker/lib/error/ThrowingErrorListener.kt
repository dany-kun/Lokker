package com.danykun.lokker.lib.error

import android.widget.ImageView

class ThrowingErrorListener : ErrorListener {
    override fun onError(imageView: ImageView, error: Throwable) {
        throw error
    }
}