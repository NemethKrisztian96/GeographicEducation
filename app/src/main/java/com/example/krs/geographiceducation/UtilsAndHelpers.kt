package com.example.krs.geographiceducation

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.example.krs.geographiceducation.model.Country
import com.example.krs.geographiceducation.study.CountryListAdapter


class UtilsAndHelpers {
    companion object {
        lateinit var countryListRecyclerViewAdapter: CountryListAdapter
        fun getImageWithGlide(context: Context, url: String, options: RequestOptions, view: ImageView) {
            Glide.with(context)
                .load(url)
                .apply(options)
                .into(view)
        }

        fun getGlideRequestOptionsForRecyclerViewItem(): RequestOptions {
            val options = RequestOptions()
                .placeholder(R.drawable.flag_placeholder)
                .error(R.drawable.flag_not_available)
                .override(64, 64)
                .centerCrop()
                .priority(Priority.HIGH)
            return options
        }

        fun getGlideRequestOptionsForBiggerImg(): RequestOptions {
            val options = RequestOptions()
                .placeholder(R.drawable.flag_placeholder)
                .error(R.drawable.flag_not_available)
                .override(128, 128)
                .centerCrop()
                .priority(Priority.HIGH)
            return options
        }

        fun getNeighborsFromBorders(borders: Array<String>): List<Country> {
            //var neighbors = ArrayList<Country>()

            return countryListRecyclerViewAdapter.mCountries.filter { c -> arrayContainsString(borders, c.mAlpha3code) }

            //return neighbors
        }

        private fun arrayContainsString(borders: Array<String>, countryCode: String): Boolean {
            for (i in 0 until borders.size) {
                if (borders[i] == countryCode) {
                    return true
                }
            }
            return false
        }
    }
}