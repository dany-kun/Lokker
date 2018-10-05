package com.danykun.lokker.lib.executor

import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import lib.RunWith
import lib.RobolectricTestRunner
import kotlinx.coroutines.experimental.timeunit.TimeUnit

@RunWith(RobolectricTestRunner::class)
class CoroutinesExecutorTest {

    class TestExecutorError : RuntimeException()

    @Test
    fun testCoroutinesWithoutError() {
        runBlocking {
            val executor: Executor = CoroutinesExecutor(this)
            executor.execute({ 1 }, { assertEquals(it, 1) })
        }
    }

    @Test(expected = TestExecutorError::class)
    fun testCoroutinesThrowing() {
        runBlocking {
            val executor: Executor = CoroutinesExecutor(this).apply {
                config.errorListener = { throwable -> throw throwable }
            }
            executor.execute({ throw TestExecutorError() }) {
                throw IllegalAccessException("This should not be called")
            }
        }
    }

    @Test(expected = TestExecutorError::class)
    fun testUiBlockThrowing() {
        runBlocking {
            val executor: Executor = CoroutinesExecutor(this).apply {
                config.errorListener = { throwable -> throw throwable }
            }
            executor.execute({ 1 }) { throw TestExecutorError() }
        }
    }

    @Test
    fun testCancelledExecution() {
        var onCompletedCall = false
        runBlocking {
            val executor: Executor = CoroutinesExecutor(this).apply {
                config.errorListener = { throw IllegalAccessException("This should not be called") }
                config.onCompleted = { onCompletedCall = true }
            }
            val request = executor.execute({ delay(10, TimeUnit.SECONDS); 1 }) {
                throw IllegalAccessException("This should not be called")
            }
            delay(1, TimeUnit.SECONDS)
            request.cancel()
        }
        assertTrue(onCompletedCall)
    }
}