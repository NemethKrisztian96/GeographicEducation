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
import com.example.krs.geographiceducation.R.*
import com.example.krs.geographiceducation.common.CountryListAdapter
import com.example.krs.geographiceducation.common.helpers.UtilsAndHelpers
import com.example.krs.geographiceducation.model.Country
import kotlinx.android.synthetic.main.fragment_country_details.view.*


class CountryDetailsFragment(country: Country) : Fragment() {
    private lateinit var mParent: StudyActivity

    var mCountry = country
    private lateinit var recyclerView: RecyclerView

    companion object {
        const val TAG = "CountryDetailsFragment"
        const val FLAG_SIZE = 64 //this is the max size

        fun newInstance(country: Country): CountryDetailsFragment {
            country.initializeMembersFromExistingRecyclerViewAdapter()
            return CountryDetailsFragment(country)
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

        val view: View =
            inflater.inflate(layout.fragment_country_details, container, false)
        view.setBackgroundColor(ContextCompat.getColor(context!!, color.white))
        view.country_name.text = mCountry.mName

        UtilsAndHelpers.fillTextViewWithColoredText(
            mParent,
            view.country_alpha2code,
            string.alpha2code,
            mCountry.mAlpha2code
        )
        UtilsAndHelpers.fillTextViewWithColoredText(
            mParent,
            view.country_area,
            string.country_area,
            mCountry.mArea.toString()
        )
        UtilsAndHelpers.fillTextViewWithColoredText(
            mParent,
            view.country_capital,
            string.country_capital,
            mCountry.mCapital
        )
        UtilsAndHelpers.fillTextViewWithColoredText(
            mParent,
            view.country_currency_exchange,
            string.country_currency_value,
            mCountry.mCurrencyValue
        )
        UtilsAndHelpers.fillTextViewWithColoredText(
            mParent,
            view.country_population,
            string.country_population,
            mCountry.mPopulation.toString()
        )
        //UtilsAndHelper.fillTextViewWithColoredText(mParent, view.country_local_time,string.country_local_time,mCountry.mLocalTime)
        UtilsAndHelpers.fillTextViewWithColoredText(
            mParent,
            view.country_local_time,
            string.country_local_time,
            mCountry.mCountryTimeZones.firstOrNull()!!
        )
        view.country_local_time_text_clock.timeZone =
            mCountry.mCountryTimeZones.firstOrNull()!!.replace("UTC", "GMT").replace(":", "")

        //get flag using glide
        UtilsAndHelpers.getImageWithGlide(
            activity as StudyActivity,
            StudyActivity.FLAG_BASE_URL + mCountry.mAlpha2code + "/flat/" + FLAG_SIZE + ".png",
            UtilsAndHelpers.getGlideRequestOptionsForBiggerImg(),
            view.country_flag
        )

        //setting toolbar
        val toolbar: Toolbar = view.findViewById(R.id.toolbar_local)
        toolbar.navigationIcon = ContextCompat.getDrawable(mParent, drawable.ic_arrow_back_white)
        toolbar.title = StudyActivity.TOOLBAR_TITLE
        mParent.setSupportActionBar(toolbar)

        toolbar.setTitleTextColor(Color.WHITE)
        toolbar.setNavigationOnClickListener { mParent.navigationOnClickListener() }

        if (mCountry.mNeighbors.isNotEmpty()) {
            recyclerView = view.neighbors_recycler_view
            recyclerView.layoutManager = LinearLayoutManager(activity)

            val adapter =
                CountryListAdapter(activity as StudyActivity) { country: Country ->
                    mParent.openCountryDetailPage(
                        country
                    )
                }
            adapter.mCountries = mCountry.mNeighbors.toMutableList()
            adapter.notifyDataSetChanged()
            recyclerView.adapter = adapter
        } else {
            view.country_neighbors_label.visibility = View.INVISIBLE
        }
        return view
    }
}