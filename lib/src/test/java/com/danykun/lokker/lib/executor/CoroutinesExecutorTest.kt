package com.danykun.lokker.lib.executor

import com.danykun.lokker.lib.LokkerImageRequest
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.concurrent.TimeUnit

@RunWith(RobolectricTestRunner::class)
class CoroutinesExecutorTest {

    class TestExecutorError : RuntimeException()

    @Test
    fun testCoroutinesWithoutError() {
        runBlocking {
            val executor: LokkerImageRequest.Executor = CoroutinesExecutor(this)
            executor.execute({ 1 }, { assertEquals(it, 1) })
        }
    }

    @Test(expected = TestExecutorError::class)
    fun testCoroutinesThrowing() {
        runBlocking {
            val executor: LokkerImageRequest.Executor = CoroutinesExecutor(this).apply {
                errorListener = { throwable -> throw throwable }
            }
            executor.execute({ throw TestExecutorError() }) {
                throw IllegalAccessException("This should not be called")
            }
        }
    }

    @Test(expected = TestExecutorError::class)
    fun testUiBlockThrowing() {
        runBlocking {
            val executor: LokkerImageRequest.Executor = CoroutinesExecutor(this).apply {
                errorListener = { throwable -> throw throwable }
            }
            executor.execute({ 1 }) { throw TestExecutorError() }
        }
    }

    @Test
    fun testCancelledExecution() {
        var onCompletedCall = false
        runBlocking {
            val executor: LokkerImageRequest.Executor = CoroutinesExecutor(this).apply {
                errorListener = { throw IllegalAccessException("This should not be called") }
                onCompleted = { onCompletedCall = true }
            }
            val request = executor.execute({ delay(1, TimeUnit.MINUTES); 1 }) {
                throw IllegalAccessException("This should not be called")
            }
            delay(1, TimeUnit.SECONDS)
            request.cancel()
        }
        assertTrue(onCompletedCall)
    }
}