package com.danykun.lokker.lib.fetch

import android.graphics.Bitmap

interface LokkerFetcher {

    suspend fun fetchImage(url: String): Bitmap
}