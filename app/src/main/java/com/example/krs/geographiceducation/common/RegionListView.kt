package com.example.krs.geographiceducation.common

import android.app.Activity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.example.krs.geographiceducation.R

/**
 * A class meant to ease the usage of the ListView that displays the regions
 */
class RegionListView {
    companion object {
        private val REGIONS: Array<String> = arrayOf("Africa", "Americas", "Asia", "Europe", "Oceania")

        fun bind(
            context: Activity,
            regionListView: ListView,
            itemOnClickListener: (AdapterView<*>, View, Int, Long) -> Unit
        ) {
            //creating and populating regionListView's adapter
            val regionAdapter = ArrayAdapter(context, R.layout.region_list_item, REGIONS)

            regionListView.adapter = regionAdapter
            regionListView.setOnItemClickListener(itemOnClickListener)
        }
    }
}