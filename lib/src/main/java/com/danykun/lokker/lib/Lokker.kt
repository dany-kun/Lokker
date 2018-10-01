package com.danykun.lokker.lib

import android.graphics.Bitmap
import android.widget.ImageView
import com.danykun.lokker.lib.cache.LokkerCache
import com.danykun.lokker.lib.fetch.LokkerFetcher
import java.lang.IllegalStateException

internal lateinit var lokkerInstance: Lokker

internal val isLokkerInitialized: Boolean
    get() = ::lokkerInstance.isInitialized


class Lokker internal constructor(
        private val fetcher: LokkerFetcher,
        private val cache: LokkerCache?) {

    internal fun loadImageInto(executor: LokkerImageRequest.Executor,
                               imageView: ImageView,
                               url: String): LokkerImageRequest {
        return executor.execute({
            retrieveBitmapFromCache(url)
                    ?: fetcher.fetchImage(url).also { storeBitmapInCache(url, it) }
        }) {
            imageView.setImageBitmap(it)
        }
    }

    private fun storeBitmapInCache(url: String, bitmap: Bitmap) {
        cache?.let {
            val urlHash = it.hasher.hashUrl(url)
            it.store.storeImage(urlHash, bitmap)
        }
    }

    private fun retrieveBitmapFromCache(url: String): Bitmap? {
        return cache?.let {
            val urlHash = it.hasher.hashUrl(url)
            it.store.retrieveImage(urlHash)
        }
    }

    class Builder(val fetcher: LokkerFetcher) {
        var cache: LokkerCache? = null
    }

    companion object {
        fun initWith(fetcher: LokkerFetcher, configBlock: Builder.() -> Unit = {}): Lokker {
            val builder = Builder(fetcher).apply(configBlock)
            return Lokker(builder.fetcher, builder.cache).also {
                initGlobalInstance(it)
            }
        }

        private fun initGlobalInstance(it: Lokker) {
            if (::lokkerInstance.isInitialized) {
                throw IllegalStateException("Lokker was already initialized")
            }
            lokkerInstance = it
        }
    }

}