package com.danykun.lokker.lib.cache

import android.graphics.Bitmap
import com.danykun.lokker.lib.cache.hash.UrlHasher

interface LokkerCache {

    /**
     * Specify how to store and retrieve the cached data
     */
    val store: Store

    /**
     * Specify how to convert an url to a key used for the cache.
     */
    val hasher: UrlHasher

    interface Store {

        fun storeImage(key: String, image: Bitmap)

        fun retrieveImage(key: String): Bitmap?
    }
}