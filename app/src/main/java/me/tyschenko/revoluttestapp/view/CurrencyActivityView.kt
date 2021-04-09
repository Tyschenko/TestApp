package me.tyschenko.revoluttestapp.view

import me.tyschenko.data.model.CurrencyRate

interface CurrencyActivityView {
    fun updateCurrencyRatesOnScreen(currencyRates: List<CurrencyRate>)
}