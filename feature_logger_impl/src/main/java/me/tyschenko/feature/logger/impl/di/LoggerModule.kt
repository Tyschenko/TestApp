package me.tyschenko.feature.logger.impl.di

import dagger.Binds
import dagger.Module
import me.tyschenko.feature.logger.api.Logger
import me.tyschenko.feature.logger.impl.LoggerImpl
import javax.inject.Singleton

@Module
internal abstract class LoggerModule {

    @Binds
    @Singleton
    abstract fun bindLogger(logger: LoggerImpl): Logger
}