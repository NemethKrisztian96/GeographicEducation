package com.example.krs.geographiceducation.study

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.krs.geographiceducation.R
import com.example.krs.geographiceducation.common.CountryListAdapter
import com.example.krs.geographiceducation.common.UtilsAndHelpers
import com.example.krs.geographiceducation.logic.Retrofit.RetrofitCountryService
import com.example.krs.geographiceducation.model.Country
import kotlinx.android.synthetic.main.fragment_country_list.*
import kotlinx.android.synthetic.main.fragment_country_list.view.*
import org.apache.commons.lang3.StringUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class CountryListFragment(region: String) : Fragment() {
    lateinit var mParent: StudyActivity
    private var mRegion = region
    private lateinit var recyclerView: RecyclerView

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
        var toolbar: Toolbar = view.findViewById(R.id.toolbar_local)
        toolbar.navigationIcon = ContextCompat.getDrawable(mParent, R.drawable.ic_arrow_back_white)
        toolbar.title = StudyActivity.TOOLBAR_TITLE
        mParent.setSupportActionBar(toolbar)

        toolbar.setTitleTextColor(Color.WHITE)
        toolbar.setNavigationOnClickListener { _ -> mParent.navigationOnClickListener() }

        recyclerView = view.findViewById<RecyclerView>(R.id.countries_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        recyclerView.adapter = adapter

        //passing the adapter for the UtilsAndHelpers class for later use
        UtilsAndHelpers.countryListRecyclerViewAdapter = adapter

        return view
    }

    fun hideLoadingProgressBar() {
        loading_progress_bar.visibility = View.INVISIBLE
    }

    private fun getDataWithRetrofit(adapter: CountryListAdapter) {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(StudyActivity.BASE_URL)  //parent.REGION_URL + selected_region + parent.FILTER_FOR_REGION_URL
            .build()
        val service = retrofit.create(RetrofitCountryService::class.java)
        val countryAssets = service.getDetailedCountries(mRegion)
        countryAssets.enqueue(object : Callback<MutableList<Country>> {
            override fun onResponse(call: Call<MutableList<Country>>, response: Response<MutableList<Country>>) {
                Log.i(TAG, "Call: success.")
                val countryAssetsData = response.body()
                if (countryAssetsData != null) {
                    adapter.mCountries = countryAssetsData
                    adapter.notifyDataSetChanged()
                    hideLoadingProgressBar()
                }
            }

            override fun onFailure(call: Call<MutableList<Country>>, t: Throwable) {
                Log.e(TAG, "Call: returned with failure.")
                hideLoadingProgressBar()
                error_image.visibility = View.VISIBLE
                Toast.makeText(context, "Could not gather data", Toast.LENGTH_LONG).show()
            }
        })
    }
}