package com.danykun.lokker.lib.cache.hash

import android.util.Base64

class AndroidBase64UrlHasher : UrlHasher {

    override fun hashUrl(url: String): String {
        return Base64.encode(url.toByteArray(), Base64.DEFAULT).toString(Charsets.UTF_8).trim()
    }
}