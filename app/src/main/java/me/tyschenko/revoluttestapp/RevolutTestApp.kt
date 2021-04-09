package me.tyschenko.revoluttestapp

import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import me.tyschenko.domain_api.di.CurrencyApi
import me.tyschenko.domain_impl.di.DaggerCurrencyComponent
import me.tyschenko.feature.logger.api.di.LoggerApi
import me.tyschenko.feature.logger.impl.di.DaggerLoggerComponent
import me.tyschenko.revoluttestapp.di.DaggerAppComponent
import javax.inject.Inject

class RevolutTestApp : Application(), HasAndroidInjector {

    @Inject
    lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder()
                .application(this)
                .currencyApi(getCurrencyApi())
                .loggerApi(getLoggerApi())
                .build()
                .inject(this)
    }

    private fun getCurrencyApi(): CurrencyApi =
            DaggerCurrencyComponent
                    .builder()
                    .build()

    private fun getLoggerApi(): LoggerApi =
            DaggerLoggerComponent
                    .builder()
                    .build()

    override fun androidInjector(): AndroidInjector<Any> = activityDispatchingAndroidInjector
}