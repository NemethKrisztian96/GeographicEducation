package com.example.krs.geographiceducation.logic

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.example.krs.geographiceducation.R
import com.example.krs.geographiceducation.logic.Retrofit.RetrofitCountryService
import com.example.krs.geographiceducation.logic.Retrofit.RetrofitExchangeRateService
import com.example.krs.geographiceducation.model.Country
import com.example.krs.geographiceducation.model.CountryCurrency
import com.example.krs.geographiceducation.model.ExchangeRate
import com.example.krs.geographiceducation.study.CountryListAdapter
import com.example.krs.geographiceducation.study.StudyActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class UtilsAndHelpers {
    companion object {
        const val TAG = "HELPER"
        lateinit var countryListRecyclerViewAdapter: CountryListAdapter
        var localCurrency: String? = null
        var exchangeRates: ExchangeRate? = null

        const val SMALL_IMG_WIDTH = 64
        const val BIG_IMG_WIDTH = 128

        fun getImageWithGlide(context: Context, url: String, options: RequestOptions, view: ImageView) {
            Glide.with(context)
                .load(url)
                .apply(options)
                .into(view)
        }

        fun getGlideRequestOptionsForRecyclerViewItem(): RequestOptions {
            return RequestOptions()
                .placeholder(R.drawable.flag_placeholder)
                .error(R.drawable.flag_not_available)
                .override(SMALL_IMG_WIDTH, SMALL_IMG_WIDTH)
                .centerCrop()
                .priority(Priority.HIGH)
        }

        fun getGlideRequestOptionsForBiggerImg(): RequestOptions {
            return RequestOptions()
                .placeholder(R.drawable.flag_placeholder)
                .error(R.drawable.flag_not_available)
                .override(BIG_IMG_WIDTH, BIG_IMG_WIDTH)
                .centerCrop()
                .priority(Priority.HIGH)
        }

        /**
         * Gets the first available exchange rate for the given country
         * @param borders the neighboring countries alpha2code
         * @return a list of Country neighbours
         */
        fun getNeighborsFromBorders(borders: Array<String>): List<Country> {
            return countryListRecyclerViewAdapter.mCountries.filter { c ->
                arrayContainsString(
                    borders,
                    c.mAlpha3code
                )
            }
        }

        private fun arrayContainsString(borders: Array<String>, countryCode: String): Boolean {
            for (i in 0 until borders.size) {
                if (borders[i] == countryCode) {
                    return true
                }
            }
            return false
        }

        /**
         * Gets the first available exchange rate for the given country
         * @param countryCode the country's alpha3code
         */
        fun getExchangeRates(countryCode: String) {
            //getting currencies used in country
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(StudyActivity.BASE_URL)  //parent.REGION_URL + selected_region + parent.FILTER_FOR_REGION_URL
                .build()
            val service = retrofit.create(RetrofitCountryService::class.java)
            val countryAssets = service.getCountriesAndCurrencies()
            countryAssets.enqueue(object : Callback<List<Country>> {
                override fun onResponse(call: Call<List<Country>>, response: Response<List<Country>>) {
                    Log.i(TAG, "Call: success.")
                    response.body()?.forEach { country ->
                        if (country.mAlpha3code == countryCode) {
                            country.mCurrencies.toList().forEach {
                                //getting exchange rate for this currency
                                val retrofit2 = Retrofit.Builder()
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .baseUrl(StudyActivity.EXCHANGE_BASE_URL)  //parent.REGION_URL + selected_region + parent.FILTER_FOR_REGION_URL
                                    .build()
                                val service2 = retrofit2.create(RetrofitExchangeRateService::class.java)
                                val exchangeAssets = service2.getExchangeRates(it.mCode)
                                exchangeAssets.enqueue(object : Callback<ExchangeRate> {
                                    override fun onResponse(
                                        call: Call<ExchangeRate>,
                                        response: Response<ExchangeRate>
                                    ) {
                                        Log.i(TAG, "Call: success.")
                                        val exchangeData = response.body()
                                        if (exchangeData != null) {
                                            //setting the exchangeRate
                                            exchangeRates = exchangeData
                                        }
                                    }

                                    override fun onFailure(call: Call<ExchangeRate>, t: Throwable) {
                                        Log.e(TAG, "Call: returned with failure. Could not find exchange rates.")
                                    }
                                })
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<List<Country>>, t: Throwable) {
                    Log.e(TAG, "Call: returned with failure. Could not find country's currency.")
                }
            })
        }

        fun getTransformedCurrency(currency: CountryCurrency): String? {
            //transform to proper rate
            return exchangeRates?.getExchangeRate(currency.mCode)
        }

        fun hasActiveInternetConnection(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
            return activeNetwork?.isConnected == true
        }


    }
}