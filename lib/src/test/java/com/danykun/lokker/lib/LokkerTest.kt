package com.danykun.lokker.lib

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import com.danykun.lokker.lib.cache.LokkerCache
import com.danykun.lokker.lib.cache.hash.AndroidBase64UrlHasher
import com.danykun.lokker.lib.error.ThrowingErrorListener
import com.danykun.lokker.lib.executor.CoroutinesExecutor
import com.danykun.lokker.lib.fetch.LokkerFetcher
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockkClass
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert.assertEquals
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class LokkerTest {

    class LokkerTestError : Exception()

    companion object {
        private val fetcher = mockkClass(LokkerFetcher::class)

        private fun createTestBitmap(color: Int): Bitmap {
            return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_4444).apply {
                setPixel(0, 0, color)
            }
        }

        private val bitmap = createTestBitmap(Color.RED)

        @BeforeClass
        @JvmStatic
        fun setup() {
            Lokker.initWith(fetcher)
        }
    }


    @Test
    fun loadImage() {
        val imageView = ImageView(RuntimeEnvironment.application)
        runBlocking {
            coEvery { fetcher.fetchImage(any()) } returns bitmap
            imageView.withLokker(this)
                .setImageFromUrl("test") {
                    errorListener = ThrowingErrorListener()
                }
        }
        val imageBitmap = (imageView.drawable as? BitmapDrawable)?.bitmap
        assertEquals(imageBitmap?.getPixel(0, 0), Color.RED)
    }

    @Test
    fun cancelLoadingImage() {
        val imageView = ImageView(RuntimeEnvironment.application)
        runBlocking {
            coEvery { fetcher.fetchImage(any()) } returns bitmap
            val request = imageView
                .withLokker(this)
                .setImageFromUrl("test") {
                    errorListener = ThrowingErrorListener()
                }
            request.cancel()
        }
        val imageBitmap = (imageView.drawable as? BitmapDrawable)?.bitmap
        assertEquals(imageBitmap?.getPixel(0, 0), null)
    }

    @Test(expected = LokkerTestError::class)
    fun errorOnLoadingImage() {
        val imageView = ImageView(RuntimeEnvironment.application)
        runBlocking {
            coEvery { fetcher.fetchImage(any()) } throws LokkerTestError()
            imageView
                .withLokker(this)
                .setImageFromUrl("test") {
                    errorListener = ThrowingErrorListener()
                }
        }
        val imageBitmap = (imageView.drawable as? BitmapDrawable)?.bitmap
        assertEquals(imageBitmap?.getPixel(0, 0), null)
    }

    @Test
    fun useCacheWhenProvided() {
        val cache = mockkClass(LokkerCache::class)
        every { cache.hasher } returns AndroidBase64UrlHasher()
        every { cache.store.retrieveImage(any()) } returns createTestBitmap(Color.BLUE)
        val imageView = ImageView(RuntimeEnvironment.application)
        val lokker = Lokker(fetcher, cache)
        runBlocking {
            coEvery { fetcher.fetchImage(any()) } throws LokkerTestError()
            lokker.loadImageInto(CoroutinesExecutor(this), imageView, "randomUrl")
        }
        val imageBitmap = (imageView.drawable as? BitmapDrawable)?.bitmap
        assertEquals(imageBitmap?.getPixel(0, 0), Color.BLUE)
    }

}