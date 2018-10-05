package com.danykun.lokker.lib.executor

import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.android.Main
import kotlin.coroutines.experimental.CoroutineContext

actual val uiCoroutinesContext: CoroutineContext = Dispatchers.Main