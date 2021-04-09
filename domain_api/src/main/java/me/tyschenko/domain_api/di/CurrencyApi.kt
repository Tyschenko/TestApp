package me.tyschenko.domain_api.di

import me.tyschenko.domain_api.CurrencyInteractor
import me.tyschenko.domain_api.CurrencyRepository

interface CurrencyApi {

    fun provideCurrencyRepository(): CurrencyRepository

    fun provideCurrencyInteractor(): CurrencyInteractor
}