package com.example.krs.geographiceducation.study

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.krs.geographiceducation.R
import com.example.krs.geographiceducation.common.RegionListView
import com.example.krs.geographiceducation.common.helpers.NavigationHelpers
import com.example.krs.geographiceducation.common.helpers.UtilsAndHelpers
import com.example.krs.geographiceducation.model.Country
import kotlinx.android.synthetic.main.activity_study.*
import java.util.*
import kotlinx.android.synthetic.main.activity_study.region_list_view as regionListView

/**
 * Activity that is responsible of the "Study" functionality
 */
class StudyActivity : AppCompatActivity() {
    private var mOpenFragments: MutableList<Fragment> = mutableListOf()

    companion object {
        private const val TAG: String = "StudyActivity"
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
        val toolbar: Toolbar = findViewById(R.id.toolbar_local)
        toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_white)
        toolbar.title = TOOLBAR_TITLE
        setSupportActionBar(toolbar)

        toolbar.setTitleTextColor(Color.WHITE)
        toolbar.setNavigationOnClickListener {
            NavigationHelpers.navigationOnClickListener(this, mOpenFragments, supportFragmentManager)
        }

        switch_button.isChecked = UtilsAndHelpers.dataSaverIsChecked
        switch_button.setOnCheckedChangeListener { buttonView, isChecked ->
            UtilsAndHelpers.dataSaverCheckedChange(buttonView, isChecked)
        }

        RegionListView.bind(
            this,
            regionListView
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

    /**
     * Handles the clicks on the navigation button
     */
    fun navigationOnClickListener() {
        if (mOpenFragments.size > 2) {
            //removing all fragments except the first (which should be CountryListFragment)
            for (i in 0 until mOpenFragments.size - 1) {
                removeLastAddedFragment()
            }
        } else {
            onBackPressed()
        }
    }

    /**
     * Removes last opened fragment for navigating back
     */
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

        //make RegionList clickable if it is on top
        if (mOpenFragments.isNullOrEmpty()) {
            regionListView.isEnabled = true
        }
    }

    /**
     * Opens the CountryListFragment for the region that has been clicked int the RegionListView
     */
    private fun selectedRegionClick(view: TextView) {
        //open fragment and pass the selected region name
        val fragment = CountryListFragment.newInstance(view.text.toString().toLowerCase())
        supportFragmentManager.beginTransaction()
            .add(R.id.activity_study, fragment)
            .commit()

        regionListView.isEnabled = false

        mOpenFragments.add(fragment)
    }

    /**
     * Opens a CountryDetailsFragment containing the data corresponding to the parameter Country
     */
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
        mOpenFragments.last()

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