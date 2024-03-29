package com.example.krs.geographiceducation.common.helpers

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.CompoundButton
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
import com.example.krs.geographiceducation.common.CountryListAdapter
import com.example.krs.geographiceducation.common.GuessGameFragment
import com.example.krs.geographiceducation.logic.retrofit.RetrofitCountryService
import com.example.krs.geographiceducation.logic.retrofit.RetrofitExchangeRateService
import com.example.krs.geographiceducation.model.Country
import com.example.krs.geographiceducation.model.CountryCurrency
import com.example.krs.geographiceducation.model.ExchangeRate
import com.example.krs.geographiceducation.play.PlayActivity
import com.example.krs.geographiceducation.study.StudyActivity
import kotlinx.android.synthetic.main.activity_play.*
import kotlinx.android.synthetic.main.fragment_country_list.error_image
import kotlinx.android.synthetic.main.fragment_country_list.loading_progress_bar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Class meant to offer all kinds of methods to facilitate different tasks
 */
class UtilsAndHelpers {
    companion object {
        const val TAG = "HELPER"
        private const val REST_COUNTRIES_BASE_URL = "https://restcountries.eu/rest/v2/"
        const val FLAG_BASE_URL =
            "https://www.countryflags.io/" //https://www.countryflags.io/:country_code/:style/:size.png

        var mExchangeRates: ExchangeRate? = null

        /**
         * Variable used to store the data saver switch's state
         */
        var dataSaverIsChecked: Boolean = false

        private const val SMALL_IMG_WIDTH = 64
        private const val BIG_IMG_WIDTH = 128

        /**
         * Method to handle data saver switch's change
         */
        fun dataSaverCheckedChange(buttonView: CompoundButton, isChecked: Boolean) {
            dataSaverIsChecked = isChecked
        }

        /**
         * Loads image from the given url into the given view according to the given request options
         */
        fun getImageWithGlide(context: Context, url: String, options: RequestOptions, view: ImageView) {
            Glide.with(context)
                .load(url)
                .apply(options)
                .into(view)
        }

        /**
         * Gets request options containing a placeholder, an error image, and small image dimensions
         */
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

        /**
         * Gets request options containing a placeholder, an error image, and bigger image dimensions
         */
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
         * Gets the neighbors from the parameter country list for the given country
         * @param borders the neighboring countries alpha2code
         * @param countries the list of countries the neighbors will be selected from
         * @return a list of Country neighbours
         */
        fun getNeighborsFromBorders(borders: Array<String>, countries: List<Country>): List<Country> {
            return countries.filter { c ->
                arrayContainsString(
                    borders,
                    c.mAlpha3code
                )
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
                    arrayContainsString(
                        borders,
                        c.mAlpha3code
                    )
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
            if (mExchangeRates != null) {
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
                                                mExchangeRates = exchangeData
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
        }

        /**
         * Returns the transformed currency using the downloaded exchange rates.
         * The result is in a form like "1 EUR=4.78 RON"
         */
        fun getTransformedCurrency(currency: CountryCurrency): String? {
            //transform to proper rate
            return mExchangeRates?.getExchangeRate(currency.mCode)
        }

        /**
         * Checks whether the device has an active internet connection or not
         */
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

        /**
         * Gets the data from the RestCountries API for the given region into the countries list.
         * Also take care of hiding loading progress bars in the given fragment, or displaying an error message
         * It is ugly... but it works well
         */
        fun getCountriesDataWithRetrofit(
            context: Context,
            regionName: String,
            countries: MutableList<Country>,
            adapter: CountryListAdapter? = null,
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

                        if (activity is PlayActivity && fragment is GuessGameFragment) {
                            activity.showNumberOfQuestions(fragment)
                        }

                        try {
                            fragment?.loading_progress_bar?.visibility = View.GONE
                        } catch (ex: Exception) {
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
                    if (activity is PlayActivity && fragment is GuessGameFragment) {
                        activity.switch_layout.visibility = View.GONE
                        activity.intro_text_view.text = activity.getString(R.string.something_went_wrong)
                        activity.region_list_view.visibility = View.GONE
                    }
                    activity?.loading_progress_bar?.visibility = View.INVISIBLE
                    activity?.error_image?.visibility = View.VISIBLE
                    Toast.makeText(context, "Could not gather data", Toast.LENGTH_LONG).show()
                }
            })
        }

        /**
         * Gets all the data from the RestCountries API.
         */
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

        /**
         * Formats the given duration of milliseconds into a form like "mm:ss"
         */
        fun transformGameDuration(gameDuration: Long): String {
            val minutes = TimeUnit.MILLISECONDS.toMinutes(gameDuration)
            val seconds = TimeUnit.MILLISECONDS.toSeconds(gameDuration)
            return String.format("%02d", minutes) + ":" + String.format("%02d", seconds - 60 * minutes)
        }
    }
}