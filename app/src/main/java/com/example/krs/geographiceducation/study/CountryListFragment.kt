package com.example.krs.geographiceducation.study

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.krs.geographiceducation.R
import com.example.krs.geographiceducation.UtilsAndHelpers
import com.example.krs.geographiceducation.model.Country
import com.example.krs.geographiceducation.study.Retrofit.RetrofitCountryService
import kotlinx.android.synthetic.main.fragment_country_list.view.*
import org.apache.commons.lang3.StringUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class CountryListFragment : Fragment() {
    lateinit var parent: StudyActivity

    companion object {
        const val TAG = "CountryListFragment"
        private lateinit var selected_region: String
        private lateinit var recyclerView: RecyclerView

        fun newInstance(region: String): CountryListFragment {
            selected_region = region
            return CountryListFragment()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is StudyActivity) {
            parent = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val activity = activity as Context
        val adapter = CountryListAdapter(parent)
        getDataWithRetrofit(adapter)
        val view: View = inflater.inflate(R.layout.fragment_country_list, container, false)
        view.setBackgroundColor(ContextCompat.getColor(context!!, R.color.white))
        view.countries_in_region.text = getString(R.string.countries_in_region, StringUtils.capitalize(selected_region))

        recyclerView = view.findViewById<RecyclerView>(R.id.countries_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        recyclerView.adapter = adapter

        //passing the adapter for the UtilsAndHelpers class for later use
        UtilsAndHelpers.countryListRecyclerViewAdapter = adapter

        return view
    }

    private fun getDataWithRetrofit(adapter: CountryListAdapter) {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(StudyActivity.BASE_URL)  //parent.REGION_URL + selected_region + parent.FILTER_FOR_REGION_URL
            .build()
        val service = retrofit.create(RetrofitCountryService::class.java)
        val countryAssets = service.getDetailedCountries(selected_region)
        countryAssets.enqueue(object : Callback<List<Country>> {
            override fun onResponse(call: Call<List<Country>>, response: Response<List<Country>>) {
                Log.i(TAG, "Call: success.")
                val countryAssetsData = response.body()
                if (countryAssetsData != null) {
                    adapter.mCountries = countryAssetsData
                    adapter.mCountries[0].initializeMembers()
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<List<Country>>, t: Throwable) {
                Log.e("vodAssetsCall", "Call: returned with failure.")
            }
        })
    }
}