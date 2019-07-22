package com.example.krs.geographiceducation.logic.Retrofit

import com.example.krs.geographiceducation.model.Country
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface RetrofitCountryService {
    @GET("region/{selected_region}")
    fun getBasicCountries(@Path("selected_region") selectedRegion: String): Call<List<Country>>  //only gets the required fields for the list of countries

    @GET("region/{selected_region}")
    fun getDetailedCountries(@Path("selected_region") selectedRegion: String): Call<MutableList<Country>>

    @GET("all?fields=alpha3Code;currencies")
    fun getCountriesAndCurrencies(): Call<List<Country>>
}