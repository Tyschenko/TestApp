package me.tyschenko.domain_impl

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.tyschenko.data.model.CurrencyRates
import me.tyschenko.domain_api.CurrencyInteractor
import me.tyschenko.domain_api.CurrencyRepository
import javax.inject.Inject

internal class CurrencyInteractorImpl @Inject constructor(
        private val currencyRepository: CurrencyRepository
) : CurrencyInteractor {

    override fun getCurrencyFromServer(): Single<CurrencyRates> {
        return currencyRepository.getCurrencyFromServer()
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
    }
}