package com.danykun.lokker.lib.error

import com.danykun.lokker.lib.ImageView

interface ErrorListener {
    fun onError(imageView: ImageView, error: Throwable)
}

