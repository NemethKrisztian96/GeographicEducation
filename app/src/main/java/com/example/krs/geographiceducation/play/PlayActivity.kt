package com.example.krs.geographiceducation.play

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.krs.geographiceducation.R
import com.example.krs.geographiceducation.common.GuessGameFragment
import com.example.krs.geographiceducation.common.RegionListView
import com.example.krs.geographiceducation.common.helpers.NavigationHelpers
import com.example.krs.geographiceducation.common.helpers.UtilsAndHelpers
import com.example.krs.geographiceducation.logic.game.GameLogic
import com.example.krs.geographiceducation.model.Country
import kotlinx.android.synthetic.main.activity_play.*
import kotlinx.android.synthetic.main.activity_play.region_list_view as regionListView

/**
 * Activity that is responsible of the "Play" functionality
 */
class PlayActivity : AppCompatActivity() {
    companion object {
        const val TOOLBAR_TITLE: String = "Play"
        private const val TAG: String = "PlayActivity"
    }

    private var mOpenFragments: MutableList<Fragment> = mutableListOf()
    private var mNextFragment: GuessGameFragment? = null
    var mCountries: MutableList<Country> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        switch_button.isChecked = UtilsAndHelpers.dataSaverIsChecked
        switch_button.setOnCheckedChangeListener { buttonView, isChecked ->
            UtilsAndHelpers.dataSaverCheckedChange(buttonView, isChecked)
        }

        //setting toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar_local)
        toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_white)
        toolbar.title = TOOLBAR_TITLE
        toolbar.setTitleTextColor(Color.WHITE)
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            switch_button.isClickable = true
            NavigationHelpers.navigationOnClickListener(this, mOpenFragments, supportFragmentManager)
        }

        button_guess_capital.setOnClickListener {
            switch_button.isClickable = false
            showRegions(GuessCapitalFragment.newInstance(GameLogic(mCountries)))
        }

        button_guess_neighbor.setOnClickListener {
            switch_button.isClickable = false
            showRegions(GuessNeighborFragment.newInstance(GameLogic(mCountries)))
        }

        button_guess_flag.setOnClickListener {
            switch_button.isClickable = false
            showRegions(GuessFlagFragment.newInstance(GameLogic(mCountries)))
        }
    }

    /**
     * Shows the ListView containing the regions and handles what is selected out of it.
     * @param fragment a game type fragment that will be passed forward for further operations.
     */
    private fun showRegions(fragment: GuessGameFragment) {
        val clickListener: (AdapterView<*>, View, Int, Long) -> Unit =
            { _: AdapterView<*>, view: View, _: Int, _: Long ->
                setRegionToFragment(
                    fragment,
                    (view as TextView).text.toString().toLowerCase()
                )
                if (UtilsAndHelpers.dataSaverIsChecked) {
                    showNumberOfQuestions(fragment)
                }
                //regionListView.visibility = View.GONE
            }


        RegionListView.bind(this, regionListView, clickListener)

        button_guess_capital.visibility = View.GONE
        button_guess_neighbor.visibility = View.GONE
        button_guess_flag.visibility = View.GONE
        intro_text_view.text = getString(R.string.string_please_select_a_region)
        regionListView.visibility = View.VISIBLE
    }

    /**
     * Sets the region name to the given fragment
     */
    private fun setRegionToFragment(fragment: GuessGameFragment, regionName: String) {
        fragment.setRegion(regionName, this)
        mNextFragment = fragment
    }

    /**
     * Shows the ListView containing the possible number of questions and handles what is selected out of it.
     * @param fragment a game type fragment that will be opened
     */
    fun showNumberOfQuestions(fragment: GuessGameFragment) {
        intro_text_view.text = getString(R.string.string_please_select_the_number_of_questions)
        val listView = regionListView
        val adapter = ArrayAdapter(this, R.layout.region_list_item, mutableListOf("5", "10", "15"))

        listView.adapter = adapter
        listView.setOnItemClickListener { _: AdapterView<*>, view: View, _: Int, _: Long ->
            if (view is TextView) {
                fragment.setNumberOfQuestions(view.text.toString().toInt())
                if (!UtilsAndHelpers.dataSaverIsChecked) {
                    loading_progress_bar.visibility = View.VISIBLE
                } else {
                    loading_progress_bar.visibility = View.GONE
                }
                intro_text_view.visibility = View.GONE
                regionListView.visibility = View.GONE
                openFragment(fragment)
            }
        }
    }

    /**
     * Opens a fragment and removes the previously opened fragment
     */
    fun openFragment(fragment: Fragment) {
        //open fragment
        supportFragmentManager.beginTransaction()
            .add(R.id.activity_play, fragment)
            .commit()

        //remove previous fragment
        if (mOpenFragments.size > 0) {
            supportFragmentManager.beginTransaction()
                .remove(mOpenFragments.last())
                .commit()
            mOpenFragments.removeAt(mOpenFragments.size - 1)
        }
        //add new fragment to list
        mOpenFragments.add(fragment)
    }

    override fun onBackPressed() {
        if (mOpenFragments.size > 0 && mOpenFragments.last() is GuessGameFragment) {
            (mOpenFragments.last() as GuessGameFragment).navigationOnClickListener()
        } else {
            if (button_guess_capital.visibility == View.GONE) {
                finish()
                startActivity(intent)
            } else {
                super.onBackPressed()
            }
        }
    }
}
