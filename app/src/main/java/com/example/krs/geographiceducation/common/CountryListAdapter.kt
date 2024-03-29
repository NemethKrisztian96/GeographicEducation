package com.example.krs.geographiceducation.common

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.krs.geographiceducation.R
import com.example.krs.geographiceducation.common.helpers.UtilsAndHelpers
import com.example.krs.geographiceducation.model.Country
import com.example.krs.geographiceducation.study.StudyActivity
import kotlinx.android.synthetic.main.country_list_item.view.*

/**
 * An implementation of RecyclerView.Adapter meant for the listing minimal details about a country in a RecyclerView
 */
class CountryListAdapter(activity: Context, clickListener: (Country) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val mActivity = activity
    var mCountries: MutableList<Country> = arrayListOf()
    private val mClickListener = clickListener

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(mActivity, mCountries[position], mClickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemLayoutView = LayoutInflater.from(parent.context)
            .inflate(R.layout.country_list_item, parent, false)

        return ViewHolder(itemLayoutView)
    }

    override fun getItemCount(): Int {
        return mCountries.size
    }
}

/**
 * An implementation of RecyclerView.ViewHolder used to inflate the elements of the RecyclerView
 */
class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val FLAG_SIZE = 64

    fun bind(context: Context, country: Country, clickListener: (Country) -> Unit) {
        itemView.country_name.text = context.getString(
            R.string.country_and_alpha_code,
            country.mName,
            country.mAlpha2code
        )

        //get flag using glide
        UtilsAndHelpers.getImageWithGlide(
            context,
            StudyActivity.FLAG_BASE_URL + country.mAlpha2code + "/flat/" + FLAG_SIZE + ".png",
            UtilsAndHelpers.getGlideRequestOptionsForRecyclerViewItem(),
            itemView.country_flag
        )

        itemView.setOnClickListener { clickListener(country) }
    }
}
