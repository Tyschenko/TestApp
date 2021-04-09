package me.tyschenko.feature.logger.api

interface Logger {
    fun logError(error: Throwable, msg: String)
}