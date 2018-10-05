package com.danykun.lokker.lib.executor

import android.arch.lifecycle.GenericLifecycleObserver
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.Main

// Largely inspired from https://gist.github.com/LouisCAD/58d3017eedb60ce00721cb32a461980f
internal val LifecycleOwner.coroutineScope: CoroutineScope
    get() = lifecycle.coroutineScope

private fun Lifecycle.createJob(cancelEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY): Job = Job().also { job ->
    addObserver(object : GenericLifecycleObserver {
        override fun onStateChanged(source: LifecycleOwner?, event: Lifecycle.Event) {
            if (event == cancelEvent) {
                removeObserver(this)
                job.cancel()
            }
        }
    })
}

private val lifecycleCoroutineScopes = mutableMapOf<Lifecycle, CoroutineScope>()

private val Lifecycle.coroutineScope: CoroutineScope
    get() = lifecycleCoroutineScopes[this]
            ?: createJob().let {
                val newScope = CoroutineScope(it + Dispatchers.Main)
                lifecycleCoroutineScopes[this] = newScope
                it.invokeOnCompletion { _ -> lifecycleCoroutineScopes -= this }
                newScope
            }