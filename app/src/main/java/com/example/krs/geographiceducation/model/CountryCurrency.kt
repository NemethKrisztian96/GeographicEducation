package com.example.krs.geographiceducation.model

import com.google.gson.annotations.SerializedName

/**
 * Data model object for describing a currency
 */
class CountryCurrency(code: String, name: String) {

    @SerializedName("code")
    var mCode = code

    @SerializedName("name")
    var mName = name

}