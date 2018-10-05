package com.danykun.lokker.lib.error

import com.danykun.lokker.lib.ImageView

class ThrowingErrorListener : ErrorListener {
    override fun onError(imageView: ImageView, error: Throwable) {
        throw error
    }
}