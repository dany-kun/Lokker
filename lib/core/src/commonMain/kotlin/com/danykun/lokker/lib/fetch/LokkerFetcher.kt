package com.danykun.lokker.lib.fetch

import com.danykun.lokker.lib.Bitmap

/**
 * Transform a remote url to a [Bitmap]. No sanity checks are made on the url format beforehand.
 */
interface LokkerFetcher {

    /**
     * @param url Image remote url (may be invalid url)
     * @return bitmap the [Bitmap] decoded from the remote url
     */
    suspend fun fetchImage(url: String): Bitmap
}