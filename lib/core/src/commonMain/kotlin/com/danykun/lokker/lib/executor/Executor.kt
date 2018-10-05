package com.danykun.lokker.lib.executor

import com.danykun.lokker.lib.LokkerImageRequest

interface Executor {

    class Config {
        var errorListener: ((Throwable) -> Unit)? = null
        var onCompleted: () -> Unit = {}
    }

    val config: Config

    fun <T> execute(block: suspend () -> T, onUi: (T) -> Unit): LokkerImageRequest
}