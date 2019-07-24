package com.example.krs.geographiceducation.model

import com.google.gson.annotations.SerializedName

class CountryCurrency(code: String, name: String) {

    @SerializedName("code")
    var mCode = code

    @SerializedName("name")
    var mName = name

}