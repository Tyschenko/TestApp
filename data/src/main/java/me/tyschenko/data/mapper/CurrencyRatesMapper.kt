package me.tyschenko.data.mapper

import me.tyschenko.data.model.CurrencyRates
import me.tyschenko.network.model.CurrencyRatesDto

fun CurrencyRatesDto.toDomain(): CurrencyRates = CurrencyRates(ratesBaseToAmountMap = this.ratesBaseToAmountMap)