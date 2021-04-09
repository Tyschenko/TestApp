package me.tyschenko.feature.logger.impl

import android.util.Log
import me.tyschenko.feature.logger.api.Logger
import javax.inject.Inject

internal class LoggerImpl @Inject constructor() : Logger {

    override fun logError(error: Throwable, msg: String) {
        Log.e(this.javaClass.simpleName, msg, error)
    }
}