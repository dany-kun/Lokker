package com.danykun.lokker.sample

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import awaitByteArray
import com.danykun.lokker.lib.fetch.LokkerFetcher
import com.github.kittinunf.fuel.httpGet

class FuelFetcher : LokkerFetcher {

    override suspend fun fetchImage(url: String): Bitmap {
        val dataStream = url.httpGet().awaitByteArray().inputStream()
        return BitmapFactory.decodeStream(dataStream)
    }
}