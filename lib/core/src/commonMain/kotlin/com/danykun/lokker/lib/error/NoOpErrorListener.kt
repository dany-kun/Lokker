package com.danykun.lokker.lib.error

import com.danykun.lokker.lib.ImageView

class NoOpErrorListener : ErrorListener {
    override fun onError(imageView: ImageView, error: Throwable) {
    }
}