package me.tyschenko.feature.logger.impl

import android.util.Log
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Test

class LoggerImplTest {

    private val logger = LoggerImpl()

    @Test
    fun `log error`() {
        mockkStatic(Log::class)

        val error: Throwable = mockk()
        val message = "some message"

        every { Log.e(logger.javaClass.simpleName, message, error) } returns 0

        logger.logError(error, message)

        verify(exactly = 1) { Log.e(logger.javaClass.simpleName, message, error) }
    }
}