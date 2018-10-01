package com.danykun.lokker.lib.executor

import android.arch.lifecycle.LifecycleOwner
import com.danykun.lokker.lib.LokkerImageRequest
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.Main

class CoroutinesExecutor(private val coroutineScope: CoroutineScope) : LokkerImageRequest.Executor, CoroutineScope by coroutineScope {

    override var errorListener: ((Throwable) -> Unit)? = null
    override var onCompleted: () -> Unit = {}

    constructor(lifecycleOwner: LifecycleOwner) : this(lifecycleOwner.coroutineScope)

    override fun <T> execute(block: suspend () -> T, onUi: (T) -> Unit): LokkerImageRequest {
        val job = launch(
                context = CoroutineExceptionHandler { _, e -> errorListener?.invoke(e) },
                onCompletion = { _ -> onCompleted() }) {
            val bitmapOp = async { block() }
            withContext(Dispatchers.Main) {
                try {
                    val bitmap = bitmapOp.await()
                    onUi(bitmap)
                } catch (e: Throwable) {
                    errorListener?.invoke(e) ?: throw e
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


