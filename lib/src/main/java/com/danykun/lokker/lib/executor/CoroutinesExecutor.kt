package com.danykun.lokker.lib.executor

import android.arch.lifecycle.LifecycleOwner
import com.danykun.lokker.lib.LokkerImageRequest
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.Main

class CoroutinesExecutor(private val coroutineScope: CoroutineScope) : Executor, CoroutineScope by coroutineScope {

    override val config: Executor.Config = Executor.Config()

    constructor(lifecycleOwner: LifecycleOwner) : this(lifecycleOwner.coroutineScope)

    override fun <T> execute(block: suspend () -> T, onUi: (T) -> Unit): LokkerImageRequest {
        val job = launch(
            context = CoroutineExceptionHandler { _, e -> config.errorListener?.invoke(e) },
            onCompletion = { _ -> config.onCompleted() }) {
            val bitmapOp = async { block() }
            withContext(Dispatchers.Main) {
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


