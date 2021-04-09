package me.tyschenko.network

import io.reactivex.Single
import me.tyschenko.network.model.CurrencyRatesDto
import me.tyschenko.network.model.DEFAULT_BASE
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("latest")
    fun getCurrencyRatesFromServer(@Query("base") baseCurrency: String = DEFAULT_BASE): Single<Response<CurrencyRatesDto>>
}