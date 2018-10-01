package com.danykun.lokker.lib.hash

interface UrlHasher {

    fun hashUrl(url: String): String
}