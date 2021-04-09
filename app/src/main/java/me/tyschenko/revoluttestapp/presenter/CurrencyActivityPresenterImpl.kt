package me.tyschenko.revoluttestapp.presenter

import io.reactivex.disposables.Disposable
import me.tyschenko.feature.logger.api.Logger
import me.tyschenko.data.model.CurrencyRates
import me.tyschenko.domain_api.CurrencyInteractor
import me.tyschenko.network.model.DEFAULT_BASE
import me.tyschenko.network.model.DEFAULT_VALUE
import me.tyschenko.data.model.CurrencyRate
import me.tyschenko.revoluttestapp.view.CurrencyActivityView
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CurrencyActivityPresenterImpl @Inject constructor(
        private val view: CurrencyActivityView,
        private val currencyInteractor: CurrencyInteractor,
        private val logger: Logger
) : CurrencyActivityPresenter {

    private var currencyDisposable: Disposable? = null

    private var selectedBase = DEFAULT_BASE
    private var selectedValue = DEFAULT_VALUE
    private lateinit var currentCurrencyRates: CurrencyRates

    override fun onCreate() {
        currencyDisposable = currencyInteractor.getCurrencyFromServer()
                .map { currencyRates ->
                    currentCurrencyRates = currencyRates
                    calculateCurrencyRatesForBaseCurrency(currencyRates)
                }
                .doOnSuccess { calculatedCurrencyRates ->
                    view.updateCurrencyRatesOnScreen(calculatedCurrencyRates)
                }
                .doOnError { logger.logError(error = it, msg = "Can't update currency rates on screen") }
                .repeatWhen { it.delay(1, TimeUnit.SECONDS) }
                .retryWhen { it.delay(1, TimeUnit.SECONDS) }
                .subscribe()
    }

    override fun onCurrencyValueChanged(base: String, value: Double) {
        selectedBase = base
        selectedValue = value
        view.updateCurrencyRatesOnScreen(calculateCurrencyRatesForBaseCurrency(currentCurrencyRates))
    }

    /**
     * Convert each currency rate to currentCurrency and multiply it by entered number (currentValue)
     */
    private fun calculateCurrencyRatesForBaseCurrency(currencyRates: CurrencyRates): List<CurrencyRate> =
        currencyRates.ratesBaseToAmountMap[selectedBase]?.let { currentPriceForOne ->
            val rateCoefficient = selectedValue / currentPriceForOne
            val multipliedCurrencyRates = currencyRates.ratesBaseToAmountMap.map {
                val currencyBase = it.key
                val currencyValue = it.value
                CurrencyRate(currencyBase, currencyValue * rateCoefficient)
            }
            multipliedCurrencyRates
        } ?: emptyList()

    override fun onDestroy() {
        currencyDisposable?.dispose()
        currencyDisposable = null
    }
}