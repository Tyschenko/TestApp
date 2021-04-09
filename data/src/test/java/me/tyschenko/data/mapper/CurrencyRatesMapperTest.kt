package me.tyschenko.data.mapper

import me.tyschenko.data.model.CurrencyRates
import me.tyschenko.network.model.CurrencyRatesDto
import org.junit.Assert.assertEquals
import org.junit.Test

internal class CurrencyRatesMapperTest {

    private val dto = CurrencyRatesDto()

    private val domain = CurrencyRates(dto.ratesBaseToAmountMap)

    @Test
    fun `correct mapping dto to domain`() {
        assertEquals(domain, dto.toDomain())
    }
}