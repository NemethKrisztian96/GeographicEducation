package com.example.krs.geographiceducation.model

import android.util.Log
import com.example.krs.geographiceducation.UtilsAndHelpers
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

class Country(
    name: String,
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

    @SerializedName("alpha2Code")
    var mAlpha2code: String = alpha2code

    @SerializedName("capital")
    var mCapital: String = capital

    @SerializedName("population")
    var mPopulation: Int = population

    @SerializedName("area")
    var mArea: Double = area

    @SerializedName("timezones")
    private var mCountryTimeZones: Array<String> = timezones

    @SerializedName("alpha3Code")
    var mAlpha3code: String = alpha3code

    @SerializedName("borders")
    private var mBorders: Array<String> = borders

    @SerializedName("currencies")
    private var mCurrencies: Array<CountryCurrency> = currencies

    var mLocalTime: String = getLocalTime(0)
    var mNeighbors: List<Country> = listOf()

    var mCurrencyValue: String = "" // “1 EUR=4.78 RON”

    init {
        Log.i("Country", "Init")
        initializeMembers()
    }

    fun initializeMembers() {
        mLocalTime = getLocalTime(0)
        mNeighbors = getNeighboursFromBordersList(mBorders)
        mCurrencyValue = getCurrencyFromCurrencyList(mCurrencies)
    }

    private fun getLocalTime(position: Int): String {
        var time = SimpleDateFormat("HH:mm:ss")
        var index = position
        if (index < 0 && index >= mCountryTimeZones.size) {
            index = 0
        }
        time.timeZone = TimeZone.getTimeZone(mCountryTimeZones[index])
        return time.format(Date())
    }

    private fun getNeighboursFromBordersList(borders: Array<String>): List<Country> {
        if (borders.isNotEmpty()) {
            return UtilsAndHelpers.getNeighborsFromBorders(borders)
        }
        return listOf()
    }

    private fun getCurrencyFromCurrencyList(currencies: Array<CountryCurrency>): String {
        for (i in 0 until currencies.size) {
            var cur = currencies[i].getTransformedCurrency()
            if (cur.isNotEmpty()) {
                return cur
            }
        }
        return ""
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