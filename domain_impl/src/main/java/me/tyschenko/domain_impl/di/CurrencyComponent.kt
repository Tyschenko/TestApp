package me.tyschenko.domain_impl.di

import dagger.Component
import me.tyschenko.domain_api.di.CurrencyApi
import me.tyschenko.network.di.NetModule
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
            NetModule::class,
            CurrencyModule::class
        ]
)
interface CurrencyComponent : CurrencyApi {

    @Component.Builder
    interface Builder {
        fun build(): CurrencyComponent
    }
}