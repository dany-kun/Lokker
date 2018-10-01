package com.danykun.lokker.lib.cache

import android.graphics.Bitmap
import com.danykun.lokker.lib.hash.UrlHasher

interface LokkerCache {

    val store: Store

    val hasher: UrlHasher

    interface Store {

        fun storeImage(key: String, image: Bitmap)

        fun retrieveImage(key: String): Bitmap?
    }
}