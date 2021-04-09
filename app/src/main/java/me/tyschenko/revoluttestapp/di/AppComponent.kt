package me.tyschenko.revoluttestapp.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import me.tyschenko.domain_api.di.CurrencyApi
import me.tyschenko.feature.logger.api.di.LoggerApi
import me.tyschenko.revoluttestapp.RevolutTestApp
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
            AndroidInjectionModule::class,
            ActivityBuilder::class
        ],
        dependencies = [
            CurrencyApi::class,
            LoggerApi::class
        ]
)
interface AppComponent : AndroidInjector<RevolutTestApp> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun currencyApi(currencyApi: CurrencyApi): Builder

        fun loggerApi(loggerApi: LoggerApi): Builder

        fun build(): AppComponent
    }

    override fun inject(revolutTestApp: RevolutTestApp)
}