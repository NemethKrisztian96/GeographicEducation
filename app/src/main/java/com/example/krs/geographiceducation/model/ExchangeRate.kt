package com.example.krs.geographiceducation.model

import com.google.gson.annotations.SerializedName

/**
 * Data model object for describing the used exchange rates
 */
class ExchangeRate(rates: ExchangeRateItem, base: String) {
    @SerializedName("rates")
    var mRates: ExchangeRateItem = rates

    @SerializedName("base")
    var mBase: String = base

    /**
     * Return the exchange rates of the given currency code in a form like “1 EUR = 4.78 RON”
     */
    fun getExchangeRate(currencyCode: String): String {
        when (currencyCode) {
            "CAD" -> {
                return "1 $currencyCode = %${"%.2f".format(1 / mRates.mCAD)} $mBase"
            }
            "HKD" -> {
                return "1 $currencyCode = ${"%.2f".format(1 / mRates.mHKD)} $mBase"
            }
            "ISK" -> {
                return "1 $currencyCode = ${"%.2f".format(1 / mRates.mISK)} $mBase"
            }
            "PHP" -> {
                return "1 $currencyCode = ${"%.2f".format(1 / mRates.mPHP)} $mBase"
            }
            "DKK" -> {
                return "1 $currencyCode = ${"%.2f".format(1 / mRates.mDKK)} $mBase"
            }
            "HUF" -> {
                return "1 $currencyCode = ${"%.2f".format(1 / mRates.mHUF)} $mBase"
            }
            "CZK" -> {
                return "1 $currencyCode = ${"%.2f".format(1 / mRates.mCZK)} $mBase"
            }
            "GBP" -> {
                return "1 $currencyCode = ${"%.2f".format(1 / mRates.mGBP)} $mBase"
            }
            "RON" -> {
                return "1 $currencyCode = ${"%.2f".format(1 / mRates.mRON)} $mBase"
            }
            "SEK" -> {
                return "1 $currencyCode = ${"%.2f".format(1 / mRates.mSEK)} $mBase"
            }
            "IDR" -> {
                return "1 $currencyCode = ${"%.2f".format(1 / mRates.mIDR)} $mBase"
            }
            "INR" -> {
                return "1 $currencyCode = ${"%.2f".format(1 / mRates.mINR)} $mBase"
            }
            "BRL" -> {
                return "1 $currencyCode = ${"%.2f".format(1 / mRates.mBRL)} $mBase"
            }
            "RUB" -> {
                return "1 $currencyCode = ${"%.2f".format(1 / mRates.mRUB)} $mBase"
            }
            "HRK" -> {
                return "1 $currencyCode = ${"%.2f".format(1 / mRates.mHRK)} $mBase"
            }
            "JPY" -> {
                return "1 $currencyCode = ${"%.2f".format(1 / mRates.mJPY)} $mBase"
            }
            "THB" -> {
                return "1 $currencyCode = ${"%.2f".format(1 / mRates.mTHB)} $mBase"
            }
            "CHF" -> {
                return "1 $currencyCode = ${"%.2f".format(1 / mRates.mCHF)} $mBase"
            }
            "EUR" -> {
                return "1 $currencyCode = ${"%.2f".format(1 / mRates.mEUR)} $mBase"
            }
            "MYR" -> {
                return "1 $currencyCode = ${"%.2f".format(1 / mRates.mMYR)} $mBase"
            }
            "BGN" -> {
                return "1 $currencyCode = ${"%.2f".format(1 / mRates.mBGN)} $mBase"
            }
            "TRY" -> {
                return "1 $currencyCode = ${"%.2f".format(1 / mRates.mTRY)} $mBase"
            }
            "CNY" -> {
                return "1 $currencyCode = ${"%.2f".format(1 / mRates.mCNY)} $mBase"
            }
            "NOK" -> {
                return "1 $currencyCode = ${"%.2f".format(1 / mRates.mNOK)} $mBase"
            }
            "NZD" -> {
                return "1 $currencyCode = ${"%.2f".format(1 / mRates.mNZD)} $mBase"
            }
            "ZAR" -> {
                return "1 $currencyCode = ${"%.2f".format(1 / mRates.mZAR)} $mBase"
            }
            "USD" -> {
                return "1 $currencyCode = ${"%.2f".format(1 / mRates.mUSD)} $mBase"
            }
            "MXN" -> {
                return "1 $currencyCode = ${"%.2f".format(1 / mRates.mMXN)} $mBase"
            }
            "SGD" -> {
                return "1 $currencyCode = ${"%.2f".format(1 / mRates.mSGD)} $mBase"
            }
            "AUD" -> {
                return "1 $currencyCode = ${"%.2f".format(1 / mRates.mAUD)} $mBase"
            }
            "ILS" -> {
                return "1 $currencyCode = ${"%.2f".format(1 / mRates.mILS)} $mBase"
            }
            "KRW" -> {
                return "1 $currencyCode = ${"%.2f".format(1 / mRates.mKRW)} $mBase"
            }
            "PLN" -> {
                return "1 $currencyCode = ${"%.2f".format(1 / mRates.mPLN)} $mBase"
            }
        }
        return ""
    }
}