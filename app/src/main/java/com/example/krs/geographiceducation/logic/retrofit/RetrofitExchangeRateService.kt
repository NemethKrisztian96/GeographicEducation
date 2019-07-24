package com.example.krs.geographiceducation.logic.retrofit

import com.example.krs.geographiceducation.model.ExchangeRate
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit service for getting the currencies from th ExchangeRates API
 */
interface RetrofitExchangeRateService {

    @GET("latest")
    fun getExchangeRates(@Query("base") selectedCurrency: String): Call<ExchangeRate>

}