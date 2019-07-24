package com.example.krs.geographiceducation.common

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.example.krs.geographiceducation.MainActivity
import com.example.krs.geographiceducation.R
import com.example.krs.geographiceducation.logic.retrofit.RetrofitCountryService
import com.example.krs.geographiceducation.logic.retrofit.RetrofitExchangeRateService
import com.example.krs.geographiceducation.model.Country
import com.example.krs.geographiceducation.model.CountryCurrency
import com.example.krs.geographiceducation.model.ExchangeRate
import com.example.krs.geographiceducation.play.PlayActivity
import com.example.krs.geographiceducation.study.StudyActivity
import kotlinx.android.synthetic.main.fragment_country_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class UtilsAndHelpers {
    companion object {
        const val TAG = "HELPER"
        private const val REST_COUNTRIES_BASE_URL = "https://restcountries.eu/rest/v2/"
        const val FLAG_BASE_URL =
            "https://www.countryflags.io/" //https://www.countryflags.io/:country_code/:style/:size.png
        lateinit var countryListRecyclerViewAdapter: CountryListAdapter
        var localCurrency: String? = null
        var exchangeRates: ExchangeRate? = null

        private const val SMALL_IMG_WIDTH = 64
        private const val BIG_IMG_WIDTH = 128

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
                .override(
                    SMALL_IMG_WIDTH,
                    SMALL_IMG_WIDTH
                )
                .centerCrop()
                .priority(Priority.HIGH)
        }

        fun getGlideRequestOptionsForBiggerImg(): RequestOptions {
            return RequestOptions()
                .placeholder(R.drawable.flag_placeholder)
                .error(R.drawable.flag_not_available)
                .override(
                    BIG_IMG_WIDTH,
                    BIG_IMG_WIDTH
                )
                .centerCrop()
                .priority(Priority.HIGH)
        }

        /**
         * Gets the neighbors from the same region for the given country
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

        /**
         * Gets the neighbors from the parameter country list for the given country
         * @param borders the neighboring countries alpha2code
         * @param countries the list of countries the neighbors will be selected from
         * @return a list of Country neighbours
         */
        fun getNeighborsFromBorders(borders: Array<String>, countries: List<Country>): List<Country> {
            return countries.filter { c ->
                arrayContainsString(borders, c.mAlpha3code)
            }
        }

        /**
         * Gets all the neighbors for the given country based on the list containing all countries in MainActivity
         * @param borders the neighboring countries alpha2code
         * @return a list of Country neighbours
         */
        fun getAllExistingNeighborsFromBorders(borders: Array<String>): List<Country> {
            if (MainActivity.allCountries.size > 0) {
                return MainActivity.allCountries.filter { c ->
                    arrayContainsString(borders, c.mAlpha3code)
                }
            }
            return listOf()
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
                .baseUrl(REST_COUNTRIES_BASE_URL)  //parent.REGION_URL + selected_region + parent.FILTER_FOR_REGION_URL
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

        /**
         * Returns the string colored so that the important detail is highlighted
         */
        fun fillTextViewWithColoredText(
            activity: Activity,
            view: TextView,
            stringResourceId: Int,
            highlightedText: String
        ) {
            view.setText(activity.getString(stringResourceId, highlightedText), TextView.BufferType.SPANNABLE)
            val span = view.text as Spannable
            span.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(activity, R.color.design_default_color_primary)),
                view.text.length - highlightedText.length,
                view.text.length,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
            )
        }

        fun getCountriesDataWithRetrofit(
            context: Context,
            regionName: String,
            countries: MutableList<Country>,
            adapter: CountryListAdapter? = null,
            //progressBar: ProgressBar? = null,
            //errorImage: ImageView? = null
            fragment: Fragment? = null,
            activity: PlayActivity? = null
        ) {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(StudyActivity.BASE_URL)  //fragment.REGION_URL + selected_region + fragment.FILTER_FOR_REGION_URL
                .build()
            val service = retrofit.create(RetrofitCountryService::class.java)
            val countryAssets = service.getDetailedCountries(regionName)
            countryAssets.enqueue(object : Callback<MutableList<Country>> {
                override fun onResponse(call: Call<MutableList<Country>>, response: Response<MutableList<Country>>) {
                    Log.i(TAG, "Call: success.")
                    val countryAssetsData = response.body()
                    if (countryAssetsData != null) {
                        countries.addAll(countryAssetsData)
                        adapter?.notifyDataSetChanged()
                        if (fragment is GuessGameFragment) {
                            activity?.showNumberOfQuestions(fragment)
                        } else {
                            fragment?.loading_progress_bar?.visibility = View.GONE
                        }
                        activity?.loading_progress_bar?.visibility = View.GONE
                    }
                }

                override fun onFailure(call: Call<MutableList<Country>>, t: Throwable) {
                    Log.e(TAG, "Call: returned with failure.")
                    try {
                        fragment?.loading_progress_bar?.visibility = View.INVISIBLE

                        fragment?.error_image?.visibility = View.VISIBLE
                    } catch (ex: Exception) {

                    }
                    activity?.loading_progress_bar?.visibility = View.INVISIBLE
                    activity?.error_image?.visibility = View.VISIBLE
                    Toast.makeText(context, "Could not gather data", Toast.LENGTH_LONG).show()
                }
            })
        }

        fun getAllCountriesDataWithRetrofit(context: Context, countries: MutableList<Country>) {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(StudyActivity.BASE_URL)  //fragment.REGION_URL + selected_region + fragment.FILTER_FOR_REGION_URL
                .build()
            val service = retrofit.create(RetrofitCountryService::class.java)
            val countryAssets = service.getAllCountries()
            countryAssets.enqueue(object : Callback<List<Country>> {
                override fun onResponse(call: Call<List<Country>>, response: Response<List<Country>>) {
                    Log.i(MainActivity.TAG, "getAllCountries: success.")
                    val countryAssetsData = response.body()
                    if (countryAssetsData != null) {
                        countries.addAll(countryAssetsData)
                    }
                }

                override fun onFailure(call: Call<List<Country>>, t: Throwable) {
                    Log.e(MainActivity.TAG, "getAllCountries: returned with failure.")
                }
            })
        }

        fun transformGameDuration(gameDuration: Long): String {
            val minutes = TimeUnit.MILLISECONDS.toMinutes(gameDuration)
            val seconds = TimeUnit.MILLISECONDS.toSeconds(gameDuration)
            return String.format("%02d", minutes) + ":" + String.format("%02d", seconds)
            //return "${minutes.toString(2)}:$seconds"
        }
    }
}