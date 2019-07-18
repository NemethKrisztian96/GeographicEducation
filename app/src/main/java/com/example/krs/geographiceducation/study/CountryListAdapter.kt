package com.example.krs.geographiceducation.study

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.krs.geographiceducation.R
import com.example.krs.geographiceducation.UtilsAndHelpers
import com.example.krs.geographiceducation.model.Country
import kotlinx.android.synthetic.main.country_list_item.view.*


class CountryListAdapter(activity: Context) : RecyclerView.Adapter<ViewHolder>() {
    val mActivity = activity
    var mCountries: List<Country> = arrayListOf()
    val FLAG_SIZE = 64

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvCountryName.text = mActivity.getString(
            R.string.country_and_alpha_code,
            mCountries[position].mName,
            mCountries[position].mAlpha2code
        )
        //holder.tvCountryAlphaCode.text = mCountries[position].mAlpha2code

        //get flag using glide
        UtilsAndHelpers.getImageWithGlide(
            mActivity,
            StudyActivity.FLAG_BASE_URL + "/" + mCountries[position].mAlpha2code + "/flat/" + FLAG_SIZE + ".png",
            UtilsAndHelpers.getGlideRequestOptionsForRecyclerViewItem(),
            holder.ivCountryFlag
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemLayoutView = LayoutInflater.from(parent.context)
            .inflate(R.layout.country_list_item, null)
        return ViewHolder(itemLayoutView)
    }

    override fun getItemCount(): Int {
        return mCountries.size
    }


}

/**
 * An implementation of RecyclerView.ViewHolder used to inflate the elements of the RecyclerView
 */
class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvCountryName: TextView = view.country_name
    //val tvCountryAlphaCode: TextView = view.country_alpha_code
    val ivCountryFlag: ImageView = view.country_flag
}
