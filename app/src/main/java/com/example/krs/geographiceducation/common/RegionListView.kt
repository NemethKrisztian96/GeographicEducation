package com.example.krs.geographiceducation.common

import android.app.Activity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.example.krs.geographiceducation.R

class RegionListView {
    companion object {
        val REGIONS: Array<String> = arrayOf("Africa", "Americas", "Asia", "Europe", "Oceania")

        fun bind(context: Activity, listViewId: Int, itemOnClickListener: (AdapterView<*>, View, Int, Long) -> Unit) {
            //creating and populating regionListView and it's adapter
            val regionListView: ListView = context.findViewById(listViewId)
            val regionAdapter = ArrayAdapter<String>(context, R.layout.region_list_item, REGIONS)

            regionListView.adapter = regionAdapter
            regionListView.setOnItemClickListener(itemOnClickListener)
        }
    }
}