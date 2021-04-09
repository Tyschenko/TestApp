package me.tyschenko.domain_api

import io.reactivex.Single
import me.tyschenko.data.model.CurrencyRates

interface CurrencyRepository {
    fun getCurrencyFromServer(): Single<CurrencyRates>
}