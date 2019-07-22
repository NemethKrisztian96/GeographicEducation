package com.example.krs.geographiceducation.play

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.krs.geographiceducation.R
import com.example.krs.geographiceducation.common.NavigationHelpers
import com.example.krs.geographiceducation.common.RegionListView
import com.example.krs.geographiceducation.model.Country
import kotlinx.android.synthetic.main.activity_play.*
import kotlinx.android.synthetic.main.activity_play.region_list_view as region_list_view1

class PlayActivity : AppCompatActivity() {
    companion object {
        const val TOOLBAR_TITLE: String = "Play"
        private val TAG: String = "PlayActivity"
    }

    private var mOpenFragments: MutableList<Fragment> = mutableListOf()
    private var mNextFragment: Fragment? = null
    var mCountries: MutableList<Country> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        //setting toolbar
        var toolbar: Toolbar = findViewById(R.id.toolbar_local)
        toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_white)
        toolbar.title = TOOLBAR_TITLE
        toolbar.setTitleTextColor(Color.WHITE)
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            NavigationHelpers.navigationOnClickListener(this, mOpenFragments, supportFragmentManager)
        }

        button_guess_capital.setOnClickListener {
            showRegions(button_guess_capital.id, GuessCapitalFragment.newInstance())
        }
    }

    private fun showRegions(buttonId: Int, fragment: Fragment) {
        var clickListener: (AdapterView<*>, View, Int, Long) -> Unit =
            { _: AdapterView<*>, _: View, _: Int, _: Long -> Log.i(TAG, "Unknown click") }
        when (fragment) {
            is GuessCapitalFragment -> clickListener = { _: AdapterView<*>, view: View, _: Int, _: Long ->
                setRegionToFragment(
                    fragment,
                    (view as TextView).text.toString().toLowerCase()
                )
            }

        }
        RegionListView.bind(this, R.id.region_list_view, clickListener)

        button_guess_capital.visibility = View.GONE
        button_guess_neighbor.visibility = View.GONE
        button_guess_flag.visibility = View.GONE
        intro_text_view.text = getString(R.string.string_please_select_a_region)
        region_list_view1.visibility = View.VISIBLE
    }

    private fun setRegionToFragment(fragment: Fragment, regionName: String) {
        when (fragment) {
            is GuessCapitalFragment -> fragment.setRegion(regionName, this)

        }
        mNextFragment = fragment

        //starting loading progress bar
        loading_progress_bar.visibility = View.VISIBLE
        intro_text_view.visibility = View.GONE
        region_list_view1.visibility = View.GONE

        loading_progress_bar.tag = loading_progress_bar.visibility
        loading_progress_bar.addOnLayoutChangeListener { v: View, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int ->
            if (loading_progress_bar.tag != loading_progress_bar.visibility) {
                //loading_progress_bar.removeOnLayoutChangeListener()
                //visibility has changed
                loading_progress_bar.tag = loading_progress_bar.visibility
                val par = parent
                if (par is PlayActivity) {
                    if (par.mNextFragment != null) {
                        par.openFragment(par.mNextFragment!!)
                    }
                }
            }

        }
    }


    fun openFragment(fragment: Fragment) {
        //open fragment and pass the selected region name
        supportFragmentManager.beginTransaction()
            .add(R.id.activity_play, fragment)
            .commit()

        mOpenFragments.add(fragment)
    }
}
