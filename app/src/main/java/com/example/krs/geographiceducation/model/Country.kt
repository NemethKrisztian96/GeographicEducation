package com.example.krs.geographiceducation.model

import android.annotation.SuppressLint
import android.util.Log
import com.example.krs.geographiceducation.common.helpers.UtilsAndHelpers
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

/**
 * Data model object for describing a country
 */
class Country(
    name: String,
    region: String,
    alpha2code: String,
    alpha3code: String,
    capital: String,
    population: Int,
    area: Double,
    timezones: Array<String>,
    borders: Array<String>,
    currencies: Array<CountryCurrency>
) {
    @SerializedName("name")
    var mName: String = name

    @SerializedName("region")
    var mRegion: String = region

    @SerializedName("alpha2Code")
    var mAlpha2code: String = alpha2code

    @SerializedName("capital")
    var mCapital: String = capital

    @SerializedName("population")
    var mPopulation: Int = population

    @SerializedName("area")
    var mArea: Double = area

    @SerializedName("timezones")
    var mCountryTimeZones: Array<String> = timezones

    @SerializedName("alpha3Code")
    var mAlpha3code: String = alpha3code

    @SerializedName("borders")
    private var mBorders: Array<String> = borders

    @SerializedName("currencies")
    var mCurrencies: Array<CountryCurrency> = currencies

    private var mLocalTime: String = getLocalTime(0)
    var mNeighbors: List<Country> = listOf()

    var mCurrencyValue: String = "unavailable" // “1 EUR=4.78 RON”

    init {
        Log.i("Country", "Init")
        initializeMembersFromAllTheCountriesList()
    }

    fun initializeMembersFromAllTheCountriesList() {
        mLocalTime = getLocalTime(0)
        mNeighbors = getNeighboursFromBordersList(mBorders)
        mCurrencyValue = getCurrencyFromCurrencyList(mCurrencies)
    }

    fun initializeMembersFromCountryList(countries: List<Country>) {
        mLocalTime = getLocalTime(0)
        mNeighbors = getNeighboursFromBordersList(mBorders, countries)
        mCurrencyValue = getCurrencyFromCurrencyList(mCurrencies)
    }

    @SuppressLint("SimpleDateFormat")
    private fun getLocalTime(position: Int): String {
        val time = SimpleDateFormat("HH:mm:ss")
        var index = position
        if (index < 0 && index >= mCountryTimeZones.size) {
            index = 0
        }
        time.timeZone = TimeZone.getTimeZone(mCountryTimeZones[index])
        return time.format(Date())
    }

    private fun getNeighboursFromBordersList(borders: Array<String>): List<Country> {
        if (borders.isNotEmpty()) {
            return UtilsAndHelpers.getAllExistingNeighborsFromBorders(borders)
        }
        return listOf()
    }

    private fun getNeighboursFromBordersList(borders: Array<String>, countries: List<Country>): List<Country> {
        if (borders.isNotEmpty()) {
            return UtilsAndHelpers.getNeighborsFromBorders(borders, countries)
        }
        return listOf()
    }

    private fun getCurrencyFromCurrencyList(currencies: Array<CountryCurrency>): String {
        for (i in 0 until currencies.size) {
            val cur = UtilsAndHelpers.getTransformedCurrency(currencies[i])
            if (cur != null && cur.isNotEmpty()) {
                return cur
            }
        }
        return "unavailable"
    }

    override fun equals(other: Any?): Boolean {
        if (other is Country) {
            if (other.mAlpha3code == this.mAlpha3code) {
                return true
            }
        }
        return false
    }
}