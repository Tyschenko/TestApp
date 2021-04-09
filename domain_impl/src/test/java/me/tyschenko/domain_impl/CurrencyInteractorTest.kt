package me.tyschenko.domain_impl

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import me.tyschenko.data.model.CurrencyRates
import me.tyschenko.domain_api.CurrencyRepository
import org.junit.Before
import org.junit.Test

internal class CurrencyInteractorTest {
    private val currencyRepository: CurrencyRepository = mock()

    private val interactor: CurrencyInteractorImpl = CurrencyInteractorImpl(
            currencyRepository = currencyRepository
    )

    @Before
    fun prepareThreads() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setSingleSchedulerHandler { Schedulers.trampoline() }
    }

    @Test
    fun `get currency from server returns correct value`() {
        val currency = mock<CurrencyRates>()
        whenever(currencyRepository.getCurrencyFromServer()).thenReturn(Single.just(currency))

        interactor.getCurrencyFromServer()
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(currency)

        verify(currencyRepository).getCurrencyFromServer()
    }
}