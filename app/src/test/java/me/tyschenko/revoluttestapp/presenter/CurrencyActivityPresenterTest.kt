package me.tyschenko.revoluttestapp.presenter

import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.TestScheduler
import me.tyschenko.data.model.CurrencyRate
import me.tyschenko.data.model.CurrencyRates
import me.tyschenko.domain_api.CurrencyInteractor
import me.tyschenko.feature.logger.api.Logger
import me.tyschenko.network.model.DEFAULT_BASE
import me.tyschenko.revoluttestapp.view.CurrencyActivityView
import org.junit.Before
import org.junit.Test
import java.util.*

internal class CurrencyActivityPresenterTest {

    private val view: CurrencyActivityView = mock()
    private val currencyInteractor: CurrencyInteractor = mock()
    private val logger: Logger = mock()

    private val presenter: CurrencyActivityPresenter = CurrencyActivityPresenterImpl(
            view = view,
            currencyInteractor = currencyInteractor,
            logger = logger
    )

    private val testScheduler = TestScheduler()

    @Before
    fun prepareThreads() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { testScheduler }
        RxJavaPlugins.setSingleSchedulerHandler { testScheduler }
    }

    @Test
    fun `get value from server`() {
        val currencyRates = CurrencyRates(TreeMap<String, Double>())

        whenever(currencyInteractor.getCurrencyFromServer()).thenReturn(Single.just(currencyRates))

        presenter.onCreate()

        verify(currencyInteractor).getCurrencyFromServer()
        verify(view).updateCurrencyRatesOnScreen(any())
    }

    @Test
    fun `retry after 1 second`() {
        val currencyRates = CurrencyRates(TreeMap<String, Double>())
        whenever(currencyInteractor.getCurrencyFromServer()).thenReturn(Single.just(currencyRates))
        presenter.onCreate()

        verify(view, times(1)).updateCurrencyRatesOnScreen(any())
        waitForSomeTime()
        verify(view, times(2)).updateCurrencyRatesOnScreen(any())
    }

    @Test
    fun `retry in case of error`() {
        whenever(currencyInteractor.getCurrencyFromServer()).thenReturn(Single.error(Exception()))
        presenter.onCreate()

        verify(view, never()).updateCurrencyRatesOnScreen(any())
        verify(logger, times(1)).logError(any(), any())

        waitForSomeTime()
        verify(view, never()).updateCurrencyRatesOnScreen(any())
        verify(logger, times(2)).logError(any(), any())
    }

    @Test
    fun `when base currency or value changed, recalculate all values and update view`() {
        whenever(currencyInteractor.getCurrencyFromServer()).thenReturn(Single.just(CurrencyRates(TreeMap<String, Double>())))
        presenter.onCreate()
        verify(view, times(1)).updateCurrencyRatesOnScreen(any())

        presenter.onCurrencyValueChanged("newBase", 100.0)

        verify(view, times(2)).updateCurrencyRatesOnScreen(any())
    }

    @Test
    fun `correct calculations with default base and amount`() {
        val currencyRates = CurrencyRates(TreeMap<String, Double>().apply {
            this["EUR"] = 2.0
            this["USD"] = 1.0
        })
        whenever(currencyInteractor.getCurrencyFromServer()).thenReturn(Single.just(currencyRates))
        presenter.onCreate()

        verify(view).updateCurrencyRatesOnScreen(
                listOf(
                        CurrencyRate("EUR", 2.0),
                        CurrencyRate("USD", 1.0)
                )
        )
    }

    @Test
    fun `correct calculations with changed amount`() {
        val currencyRates = CurrencyRates(TreeMap<String, Double>().apply {
            this["EUR"] = 2.0
            this["USD"] = 1.0
        })
        whenever(currencyInteractor.getCurrencyFromServer()).thenReturn(Single.just(currencyRates))
        presenter.onCreate()

        val newAmount = 500.0
        presenter.onCurrencyValueChanged(DEFAULT_BASE, newAmount)

        verify(view).updateCurrencyRatesOnScreen(
                listOf(
                        CurrencyRate("EUR", 2.0 * newAmount),
                        CurrencyRate("USD", 1.0 * newAmount)
                )
        )
    }

    @Test
    fun `correct calculations with changed base currency`() {
        val currencyRates = CurrencyRates(TreeMap<String, Double>().apply {
            this["EUR"] = 2.0
            this["USD"] = 1.0
        })
        whenever(currencyInteractor.getCurrencyFromServer()).thenReturn(Single.just(currencyRates))
        presenter.onCreate()

        presenter.onCurrencyValueChanged("EUR", 1.0)

        verify(view).updateCurrencyRatesOnScreen(
                listOf(
                        CurrencyRate("EUR", 1.0),
                        CurrencyRate("USD", 0.5)
                )
        )
    }

    private fun waitForSomeTime() {
        // Wait a bit more than a second to make tests non flaky
        Thread.sleep(1010)
    }
}