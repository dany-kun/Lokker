package com.danykun.lokker.lib.cache.hash

interface UrlHasher {

    fun hashUrl(url: String): String
}