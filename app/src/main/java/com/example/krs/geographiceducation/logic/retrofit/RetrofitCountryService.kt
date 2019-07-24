package com.example.krs.geographiceducation.logic.retrofit

import com.example.krs.geographiceducation.model.Country
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Retrofit service for getting the countries from the RestCountries API
 */
interface RetrofitCountryService {
    /**
     * Gets all the data available
     */
    @GET("all")
    fun getAllCountries(): Call<List<Country>>  //only gets the required fields for the list of countries

    /**
     * Gets the data needed for the country list RecyclerView corresponding to a given region
     */
    @GET("region/{selected_region}?fields=name,alpha2Code") // ?fields=name,alpha2Code
    fun getBasicCountries(@Path("selected_region") selectedRegion: String): Call<List<Country>>  //only gets the required fields for the list of countries

    /**
     * Gets the data corresponding to a given region
     */
    @GET("region/{selected_region}")
    fun getDetailedCountries(@Path("selected_region") selectedRegion: String): Call<MutableList<Country>>

    /**
     * Filters the results and keeps only the alpha3codes and the corresponding currencies
     */
    @GET("all?fields=alpha3Code;currencies")
    fun getCountriesAndCurrencies(): Call<List<Country>>
}