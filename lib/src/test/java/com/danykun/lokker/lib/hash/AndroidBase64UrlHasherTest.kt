package com.danykun.lokker.lib.hash

import org.junit.Test

import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AndroidBase64UrlHasherTest {

    private val hasher = AndroidBase64UrlHasher()

    @Test
    fun hashUrl() {
        assertEquals("", hasher.hashUrl(""))
        assertEquals( "dGVzdA==", hasher.hashUrl("test"))
        assertEquals("dGVzdDI=", hasher.hashUrl("test2"))
    }
}