package com.danykun.lokker.lib

import android.arch.lifecycle.LifecycleOwner
import android.widget.ImageView
import com.danykun.lokker.lib.executor.CoroutinesExecutor
import com.danykun.lokker.lib.executor.Executor
import kotlinx.coroutines.experimental.CoroutineScope
import java.lang.IllegalStateException

fun ImageView.withUnmanagedLokker(executor: Executor): LokkerImageRequest.Builder {
    if (!isLokkerInitialized) {
        throw IllegalStateException("Alogoloader should be initialized with Lokker.intiWith")
    }
    // This could be improved to prevent the ImqgeView from leaking in case the builder is not used
    return LokkerImageRequest.Builder(lokkerInstance, executor, this)
}

fun ImageView.withLokker(lifecycleOwner: LifecycleOwner): LokkerImageRequest.Builder {
    return withUnmanagedLokker(CoroutinesExecutor(lifecycleOwner))
}

fun ImageView.withLokker(coroutineScope: CoroutineScope): LokkerImageRequest.Builder {
    return withUnmanagedLokker(CoroutinesExecutor(coroutineScope))
}