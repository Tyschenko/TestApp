package me.tyschenko.feature.logger.api.di

import me.tyschenko.feature.logger.api.Logger

interface LoggerApi {
    fun provideLogger(): Logger
}