package com.example.krs.geographiceducation.study

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.TextView.BufferType
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.krs.geographiceducation.R
import com.example.krs.geographiceducation.R.*
import com.example.krs.geographiceducation.common.CountryListAdapter
import com.example.krs.geographiceducation.common.UtilsAndHelpers
import com.example.krs.geographiceducation.model.Country
import kotlinx.android.synthetic.main.fragment_country_details.view.*


class CountryDetailsFragment(country: Country) : Fragment() {
    lateinit var mParent: StudyActivity

    private var mCountry = country
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

        fillTextViewWithColoredText(view.country_alpha2code, string.alpha2code, mCountry.mAlpha2code)
        fillTextViewWithColoredText(view.country_area, string.country_area, mCountry.mArea.toString())
        fillTextViewWithColoredText(view.country_capital, string.country_capital, mCountry.mCapital)
        fillTextViewWithColoredText(
            view.country_currency_exchange,
            string.country_currency_value,
            mCountry.mCurrencyValue
        )
        fillTextViewWithColoredText(view.country_population, string.country_population, mCountry.mPopulation.toString())
        //fillTextViewWithColoredText(view.country_local_time,string.country_local_time,mCountry.mLocalTime)
        fillTextViewWithColoredText(
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
        var toolbar: Toolbar = view.findViewById(R.id.toolbar_local)
        toolbar.navigationIcon = ContextCompat.getDrawable(mParent, R.drawable.ic_arrow_back_white)
        toolbar.title = StudyActivity.TOOLBAR_TITLE
        mParent.setSupportActionBar(toolbar)

        toolbar.setTitleTextColor(Color.WHITE)
        toolbar.setNavigationOnClickListener { _ -> mParent.navigationOnClickListener() }

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

    /**
     * Returns the string colored so that the important detail is highlighted
     */
    private fun fillTextViewWithColoredText(view: TextView, stringResourceId: Int, highlightedText: String) {
        view.setText(getString(stringResourceId, highlightedText), BufferType.SPANNABLE)
        val span = view.text as Spannable
        span.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(context!!, color.design_default_color_primary)),
            view.text.length - highlightedText.length,
            view.text.length,
            Spannable.SPAN_INCLUSIVE_EXCLUSIVE
        )
    }
}