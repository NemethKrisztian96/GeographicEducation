package com.example.krs.geographiceducation.study

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.krs.geographiceducation.R

class StudyActivity : AppCompatActivity() {
    private val TAG: String = "StudyActivity"
    private lateinit var regionListView: ListView
    private val REGIONS: Array<String> = arrayOf("Africa", "Americas", "Asia", "Europe", "Oceania")

    companion object {
        val BASE_URL = "https://restcountries.eu/rest/v2/"
        val REGION_URL = "https://restcountries.eu/rest/v2/region/" // + /region_name
        val FILTER_FOR_REGION_URL = "?fields=name,alpha2Code/"
        val FLAG_BASE_URL = "https://www.countryflags.io/" //https://www.countryflags.io/:country_code/:style/:size.png
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_study)

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
    }

    fun selectedRegionClick(view: TextView) {
        //open fragment and pass the selected region name
        supportFragmentManager.beginTransaction()
            .add(R.id.activity_study, CountryListFragment.newInstance(view.text.toString().toLowerCase()))
            .commit()
    }
}