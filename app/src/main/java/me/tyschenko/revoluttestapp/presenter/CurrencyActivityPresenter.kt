package me.tyschenko.revoluttestapp.presenter

interface CurrencyActivityPresenter {
    fun onCurrencyValueChanged(base: String, value: Double)
    fun onCreate()
    fun onDestroy()
}