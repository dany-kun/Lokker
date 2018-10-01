package com.danykun.lokker.lib.error

import android.widget.ImageView

interface ErrorListener {
    fun onError(imageView: ImageView, error: Throwable)
}

