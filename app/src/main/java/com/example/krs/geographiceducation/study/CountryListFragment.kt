package com.example.krs.geographiceducation.study

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.krs.geographiceducation.R
import com.example.krs.geographiceducation.common.CountryListAdapter
import com.example.krs.geographiceducation.common.helpers.UtilsAndHelpers
import com.example.krs.geographiceducation.model.Country
import kotlinx.android.synthetic.main.fragment_country_list.view.*
import org.apache.commons.lang3.StringUtils

/**
 * Fragment that displays a list (RecyclerView) with minimal details about Countries in a region
 */
class CountryListFragment(region: String) : Fragment() {
    private lateinit var mParent: StudyActivity
    private var mRegion = region
    private lateinit var mRecyclerView: RecyclerView

    companion object {
        const val TAG = "CountryListFragment"

        fun newInstance(region: String): CountryListFragment {
            return CountryListFragment(region)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is StudyActivity) {
            mParent = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val activity = activity as Context
        val adapter = CountryListAdapter(mParent) { country: Country ->
            mParent.openCountryDetailPage(country)
        }

        //getDataWithRetrofit(adapter)
        UtilsAndHelpers.getCountriesDataWithRetrofit(context!!, mRegion, adapter.mCountries, adapter, this)

        val view: View = inflater.inflate(R.layout.fragment_country_list, container, false)
        view.setBackgroundColor(ContextCompat.getColor(context!!, R.color.white))
        view.countries_in_region.text = getString(R.string.countries_in_region, StringUtils.capitalize(mRegion))

        //setting toolbar
        val toolbar: Toolbar = view.findViewById(R.id.toolbar_local)
        toolbar.navigationIcon = ContextCompat.getDrawable(mParent, R.drawable.ic_arrow_back_white)
        toolbar.title = StudyActivity.TOOLBAR_TITLE
        mParent.setSupportActionBar(toolbar)

        toolbar.setTitleTextColor(Color.WHITE)
        toolbar.setNavigationOnClickListener { mParent.navigationOnClickListener() }

        mRecyclerView = view.findViewById(R.id.countries_recycler_view)
        mRecyclerView.layoutManager = LinearLayoutManager(activity)

        mRecyclerView.adapter = adapter

        return view
    }
}