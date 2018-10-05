package com.danykun.lokker.lib.executor

import com.danykun.lokker.lib.LokkerImageRequest
import kotlinx.coroutines.experimental.CoroutineExceptionHandler
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext

class CoroutinesExecutor(private val coroutineScope: CoroutineScope) : Executor, CoroutineScope by coroutineScope {

    override val config: Executor.Config = Executor.Config()

    override fun <T> execute(block: suspend () -> T, onUi: (T) -> Unit): LokkerImageRequest {
        val job = launch(
            context = CoroutineExceptionHandler { _, e -> config.errorListener?.invoke(e) },
            onCompletion = { _ -> config.onCompleted() }) {
            val bitmapOp = async { block() }
            withContext(uiCoroutinesContext) {
                try {
                    val bitmap = bitmapOp.await()
                    onUi(bitmap)
                } catch (e: Throwable) {
                    config.errorListener?.invoke(e) ?: throw e
                }
            }
        }
        return object : LokkerImageRequest {
            override fun cancel() {
                job.cancel()
            }
        }
    }
}


