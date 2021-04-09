package me.tyschenko.domain_impl.di

import dagger.Binds
import dagger.Module
import me.tyschenko.domain_api.CurrencyInteractor
import me.tyschenko.domain_api.CurrencyRepository
import me.tyschenko.domain_impl.CurrencyInteractorImpl
import me.tyschenko.domain_impl.CurrencyRepositoryImpl
import javax.inject.Singleton

@Module
internal abstract class CurrencyModule {

    @Binds
    @Singleton
    abstract fun bindCurrencyRepository(currencyRepositoryImpl: CurrencyRepositoryImpl): CurrencyRepository

    @Binds
    @Singleton
    abstract fun bindCurrencyInteractor(currencyInteractorImpl: CurrencyInteractorImpl): CurrencyInteractor
}