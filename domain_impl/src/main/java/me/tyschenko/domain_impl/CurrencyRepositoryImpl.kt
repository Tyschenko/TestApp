package me.tyschenko.domain_impl

import io.reactivex.Single
import me.tyschenko.data.mapper.toDomain
import me.tyschenko.data.model.CurrencyRates
import me.tyschenko.domain_api.CurrencyRepository
import me.tyschenko.network.ApiInterface
import javax.inject.Inject

internal class CurrencyRepositoryImpl @Inject constructor(
        private val apiInterface: ApiInterface
) : CurrencyRepository {

    override fun getCurrencyFromServer(): Single<CurrencyRates> {
        return apiInterface.getCurrencyRatesFromServer()
                .flatMap {
                    val body = it.body()
                    if (it.isSuccessful && body != null && body.error.isEmpty()) {
                        Single.just(body.toDomain())
                    } else {
                        Single.error(Throwable(it.body()?.error.toString()))
                    }
                }
    }
}