package com.example.krs.geographiceducation.study

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.krs.geographiceducation.R
import com.example.krs.geographiceducation.common.NavigationHelpers
import com.example.krs.geographiceducation.common.RegionListView
import com.example.krs.geographiceducation.common.UtilsAndHelpers
import com.example.krs.geographiceducation.model.Country
import java.util.*

class StudyActivity : AppCompatActivity() {
    //private lateinit var regionListView: ListView
    var mOpenFragments: MutableList<Fragment> = mutableListOf()

    companion object {
        private val TAG: String = "StudyActivity"
        const val TOOLBAR_TITLE: String = "Study"
        const val BASE_URL = "https://restcountries.eu/rest/v2/"
        //const val REGION_URL = "https://restcountries.eu/rest/v2/region/" // + /region_name
        //const val FILTER_FOR_REGION_URL = "?fields=name,alpha2Code/"
        const val FLAG_BASE_URL =
            "https://www.countryflags.io/" //https://www.countryflags.io/:country_code/:style/:size.png
        const val EXCHANGE_BASE_URL =
            "https://api.exchangeratesapi.io/" //https://api.exchangeratesapi.io/latest?base=RON
        //var exchangeRate: ExchangeRate? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_study)

        //setting toolbar
        var toolbar: Toolbar = findViewById(R.id.toolbar_local)
        toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_white)
        toolbar.title = TOOLBAR_TITLE
        setSupportActionBar(toolbar)

        toolbar.setTitleTextColor(Color.WHITE)
        toolbar.setNavigationOnClickListener {
            NavigationHelpers.navigationOnClickListener(this, mOpenFragments, supportFragmentManager)
        }

        RegionListView.bind(
            this,
            R.id.region_list_view
        ) { parent, view, position, id -> selectedRegionClick(view as TextView) }

        //get exchange rates
        val localCountry = Locale.getDefault().isO3Country
        UtilsAndHelpers.getExchangeRates(localCountry)
    }

    override fun onBackPressed() {
        if (mOpenFragments.isNotEmpty()) {
            removeLastAddedFragment()
        } else {
            super.onBackPressed()
        }
    }

    fun navigationOnClickListener() {
        if (mOpenFragments.size > 1) {
            //removing all fragments except the first (which should be CountryListFragment)
            for (i in 0 until mOpenFragments.size - 1) {
                removeLastAddedFragment()
            }
        } else {
            onBackPressed()
        }
    }

    private fun removeLastAddedFragment() {
        val fragment = mOpenFragments.last()

        if (fragment is CountryDetailsFragment) {
            //check if there are duplicate references
            var reused = false
            mOpenFragments.forEach { frag: Fragment ->
                if (!reused && frag is CountryDetailsFragment) {
                    if (frag.mCountry == fragment.mCountry) {
                        reused = true
                    }
                }
            }

            if (reused) {
                //hide current
                supportFragmentManager.beginTransaction()
                    .hide(fragment)
                    .commit()
                //show previous
                supportFragmentManager.beginTransaction()
                    .show(mOpenFragments[mOpenFragments.size - 2])
                    .commit()
            } else {
                supportFragmentManager.beginTransaction()
                    .remove(fragment)
                    .commit()
            }
        } else {
            supportFragmentManager.beginTransaction()
                .remove(fragment)
                .commit()
        }

        //remove last
        mOpenFragments.removeAt(mOpenFragments.size - 1)
    }

    private fun selectedRegionClick(view: TextView) {
        //open fragment and pass the selected region name
        val fragment = CountryListFragment.newInstance(view.text.toString().toLowerCase())
        supportFragmentManager.beginTransaction()
            .add(R.id.activity_study, fragment)
            .commit()

        mOpenFragments.add(fragment)
    }

    fun openCountryDetailPage(country: Country) {
        //reuse already opened fragment if it exists
        var reused = false
        var fragment: Fragment? = null
        mOpenFragments.forEach { frag: Fragment ->
            if (!reused && frag is CountryDetailsFragment) {
                if (frag.mCountry == country) {
                    reused = true
                    fragment = frag
                }
            }
        }

        //hiding last fragment
        supportFragmentManager.beginTransaction()
            .hide(mOpenFragments.last())
            .commit()

        if (!reused) {
            //open fragment and pass the selected region name
            fragment = CountryDetailsFragment.newInstance(country)
            supportFragmentManager.beginTransaction()
                .add(R.id.activity_study, fragment!!)
                .commit()
        } else {
            supportFragmentManager.beginTransaction()
                .show(fragment!!)
                .commit()
        }

        mOpenFragments.add(fragment!!)
    }

}