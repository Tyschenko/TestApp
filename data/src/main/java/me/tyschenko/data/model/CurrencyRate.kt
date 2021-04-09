package me.tyschenko.data.model

import kotlin.math.roundToLong

data class CurrencyRate(val base: String, val amount: Double) {

    fun getCurrencyAmountAsString(): String {
        val roundedValue = ((amount * 100.0).roundToLong() / 100.0) // Round to 2 digits after comma
        val roundedToLongValue = roundedValue.toLong()
        return if (roundedValue - roundedToLongValue == 0.0) {
            roundedToLongValue.toString()
        } else {
            roundedValue.toBigDecimal().toPlainString() // Converting to BigDecimal removes scientific notation
        }
    }
}