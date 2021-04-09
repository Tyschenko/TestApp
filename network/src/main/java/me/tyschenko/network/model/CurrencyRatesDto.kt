package me.tyschenko.network.model

import com.google.gson.annotations.SerializedName
import java.util.*

const val DEFAULT_BASE = "USD"
const val DEFAULT_VALUE = 1.0

class CurrencyRatesDto {

    @SerializedName("base")
    private val base: String = ""

    @SerializedName("error")
    val error: String = ""

    @SerializedName("rates")
    val ratesBaseToAmountMap: TreeMap<String, Double> = TreeMap()
        get() {
            // When we request currency from API, we need to add base and it's amount 1.0 to result
            if (field.isNotEmpty() && base.isNotEmpty() && !field.containsKey(base)) {
                field[base] = DEFAULT_VALUE
            }
            return field
        }
}