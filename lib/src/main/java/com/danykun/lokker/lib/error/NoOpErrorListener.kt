package com.danykun.lokker.lib.error

import android.widget.ImageView

class NoOpErrorListener : ErrorListener {
    override fun onError(imageView: ImageView, error: Throwable) {
    }
}