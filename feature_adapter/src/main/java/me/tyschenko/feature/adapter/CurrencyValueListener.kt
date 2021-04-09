package me.tyschenko.feature.adapter

interface CurrencyValueListener {
    fun onCurrencyValueChanged(base: String, value: Double)
    fun onCurrencyValueClicked()
}