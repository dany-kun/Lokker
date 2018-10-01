package com.danykun.lokker.lib

import android.arch.lifecycle.LifecycleOwner
import android.widget.ImageView
import com.danykun.lokker.lib.executor.CoroutinesExecutor
import com.danykun.lokker.lib.executor.Executor
import kotlinx.coroutines.experimental.CoroutineScope

/**
 * Create a [LokkerImageRequest.Builder] with an executor.
 *
 * **Important**: This does not manage the execution.
 * It is client responsibility to handle the execution lifecycle once the request created.
 *
 * @receiver [ImageView] targeted by the request
 * @param [executor] in which the image request will be executed
 * @return [LokkerImageRequest.Builder] to configure and start the [LokkerImageRequest]
 */
fun ImageView.withUnmanagedLokker(executor: Executor): LokkerImageRequest.Builder {
    if (!isLokkerInitialized) {
        throw IllegalStateException("Alogoloader should be initialized with Lokker.intiWith")
    }
    // This could be improved to prevent the ImqgeView from leaking in case the builder is not used
    return LokkerImageRequest.Builder(lokkerInstance, executor, this)
}

/**
 * Create a [LokkerImageRequest.Builder] with an executor bound to a [LifecycleOwner].
 * The request will cancel itself once the [ON_DESTROY][android.arch.lifecycle.Lifecycle.Event.ON_DESTROY] event is called
 *
 * @receiver [ImageView] targeted by the request
 * @param lifecycleOwner[LifecycleOwner] bound to the request
 * @return [LokkerImageRequest.Builder] to configure and start the [LokkerImageRequest]
 */
fun ImageView.withLokker(lifecycleOwner: LifecycleOwner): LokkerImageRequest.Builder {
    return withUnmanagedLokker(CoroutinesExecutor(lifecycleOwner))
}

/**
 * Create a [LokkerImageRequest.Builder] with an executor bound to a [CoroutineScope].
 *
 * @receiver [ImageView] targeted by the request
 * @param [coroutineScope][CoroutineScope] bound to the request
 * @return [LokkerImageRequest.Builder] to configure and start the [LokkerImageRequest]
 */
fun ImageView.withLokker(coroutineScope: CoroutineScope): LokkerImageRequest.Builder {
    return withUnmanagedLokker(CoroutinesExecutor(coroutineScope))
}