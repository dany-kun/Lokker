package com.danykun.lokker.lib

import android.graphics.Bitmap
import android.widget.ImageView
import com.danykun.lokker.lib.cache.LokkerCache
import com.danykun.lokker.lib.executor.Executor
import com.danykun.lokker.lib.fetch.LokkerFetcher

internal lateinit var lokkerInstance: Lokker

internal val isLokkerInitialized: Boolean
    get() = ::lokkerInstance.isInitialized


class Lokker internal constructor(
        private val fetcher: LokkerFetcher,
        private val cache: LokkerCache?) {

    internal fun loadImageInto(executor: Executor,
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

    /**
     * Configuration [Lokker] instances
     */
    class Builder(internal val fetcher: LokkerFetcher) {
        /**
         * [LokkerCache] to prevent making useless calls if the required url request [Bitmap] result is cached
         */
        var cache: LokkerCache? = null
    }

    companion object {
        /**
         * Specify the [LokkerFetcher] and other configuration to be used by Lokker.
         * This method setup a global and unique [Lokker] instance.
         * Must be called exactly once before making any request.
         *
         * @param fetcher [LokkerFetcher] to specify how get a [bitmap][Bitmap] from an url
         * @param configBlock Config block to add additional configuration to the [Lokker] instance. See [Builder]
         */
        fun initWith(fetcher: LokkerFetcher, configBlock: Builder.() -> Unit = {}) {
            val builder = Builder(fetcher).apply(configBlock)
            Lokker(builder.fetcher, builder.cache).also {
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