package me.tyschenko.data.model

import org.junit.Assert.assertEquals
import org.junit.Test

internal class CurrencyRateTest {
    @Test
    fun testZeroAmount() {
        val currencyRate = CurrencyRate("USD", 0.0)
        assertEquals(currencyRate.getCurrencyAmountAsString(), "0")
    }

    @Test
    fun testIntegerAmount() {
        val currencyRate = CurrencyRate("USD", 123.0)
        assertEquals(currencyRate.getCurrencyAmountAsString(), "123")
    }

    @Test
    fun testFloatAmount() {
        val currencyRate = CurrencyRate("USD", 123.2)
        assertEquals(currencyRate.getCurrencyAmountAsString(), "123.2")
    }

    @Test
    fun `Test float with large number of decimal places`() {
        val currencyRate = CurrencyRate("USD", 123.2345678)
        assertEquals(currencyRate.getCurrencyAmountAsString(), "123.23")
    }

    @Test
    fun `Test float with large number of decimal places, should be rounded up`() {
        val currencyRate = CurrencyRate("USD", 123.2399999)
        assertEquals(currencyRate.getCurrencyAmountAsString(), "123.24")
    }

    @Test
    fun testLongAmount() {
        val currencyRate = CurrencyRate("USD", 999999999999999.0)
        assertEquals(currencyRate.getCurrencyAmountAsString(), "999999999999999")
    }

    @Test
    fun testDoubleAmount() {
        val currencyRate = CurrencyRate("USD", 999999999999.111111)
        assertEquals(currencyRate.getCurrencyAmountAsString(), "999999999999.11")
    }

    @Test
    fun testDoubleAmountWithScientificNotation() {
        val currencyRate = CurrencyRate("USD", 1.3954e+10)
        assertEquals(currencyRate.getCurrencyAmountAsString(), "13954000000")
    }
}