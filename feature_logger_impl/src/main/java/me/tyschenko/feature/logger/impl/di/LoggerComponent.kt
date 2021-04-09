package me.tyschenko.feature.logger.impl.di

import dagger.Component
import me.tyschenko.feature.logger.api.di.LoggerApi
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
            LoggerModule::class
        ]
)
interface LoggerComponent : LoggerApi {

    @Component.Builder
    interface Builder {
        fun build(): LoggerComponent
    }
}