package com.example.krs.geographiceducation.study

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.krs.geographiceducation.R
import com.example.krs.geographiceducation.logic.NavigationHelpers
import com.example.krs.geographiceducation.logic.UtilsAndHelpers
import com.example.krs.geographiceducation.model.Country
import com.example.krs.geographiceducation.model.ExchangeRate
import java.util.*

class StudyActivity : AppCompatActivity() {
    private lateinit var regionListView: ListView
    private val REGIONS: Array<String> = arrayOf("Africa", "Americas", "Asia", "Europe", "Oceania")
    private var mOpenFragments: MutableList<Fragment> = mutableListOf()

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
        var exchangeRate: ExchangeRate? = null
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


        //creating and populating regionListView and it's adapter
        regionListView = findViewById(R.id.region_list_view)
        val regionAdapter = ArrayAdapter<String>(this, R.layout.region_list_item, REGIONS)
        //using a built-in list item xml
        //val regionAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, REGIONS)

        regionListView.adapter = regionAdapter
        regionListView.setOnItemClickListener { parent, view, position, id ->
            if (view is TextView) {
                Log.i(TAG, "Click!")
                selectedRegionClick(view)
            }
        }

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
        supportFragmentManager.beginTransaction()
            .remove(fragment)
            .commit()
        mOpenFragments.remove(fragment)
    }

    private fun selectedRegionClick(view: TextView) {
        //open fragment and pass the selected region name
        var fragment = CountryListFragment.newInstance(view.text.toString().toLowerCase())
        supportFragmentManager.beginTransaction()
            .add(R.id.activity_study, fragment)
            .commit()

        mOpenFragments.add(fragment)
    }

    fun openCountryDetailPage(country: Country) {
        //open fragment and pass the selected region name
        var fragment = CountryDetailsFragment.newInstance(country)
        supportFragmentManager.beginTransaction()
            .add(R.id.activity_study, fragment)
            .commit()

        mOpenFragments.add(fragment)
    }

}