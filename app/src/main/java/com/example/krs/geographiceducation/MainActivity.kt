package com.example.krs.geographiceducation

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.krs.geographiceducation.common.helpers.UtilsAndHelpers
import com.example.krs.geographiceducation.model.Country
import com.example.krs.geographiceducation.play.PlayActivity
import com.example.krs.geographiceducation.statistics.StatisticsActivity
import com.example.krs.geographiceducation.study.StudyActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.no_internet_connection_view.*

/**
 * Activity that allows the user to choose which of the three main functions wants to use
 */
class MainActivity : AppCompatActivity() {
    companion object {
        const val TOOLBAR_TITLE: String = "Home"
        const val TAG: String = "MainActivity"

        var allCountries: MutableList<Country> = mutableListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //checking internet connection state
        if (UtilsAndHelpers.hasActiveInternetConnection(this)) {
            setContentView(R.layout.activity_main)

            //setting toolbar
            val toolbar: Toolbar = findViewById(R.id.toolbar)
            toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_home_white)
            toolbar.title = TOOLBAR_TITLE
            toolbar.setTitleTextColor(Color.WHITE)
            setSupportActionBar(toolbar)


            study_button.setOnClickListener {
                studyButtonClick()
            }
            play_button.setOnClickListener {
                playButtonClick()
            }
            statistics_button.setOnClickListener {
                statisticsButtonClick()
            }

            UtilsAndHelpers.getAllCountriesDataWithRetrofit(this, allCountries)
        } else {
            //error message
            setContentView(R.layout.no_internet_connection_view)

            button_reload.setOnClickListener {
                finish()
                startActivity(intent)
            }
        }

    }

    private fun studyButtonClick() {
        //starting new activity
        val intent = Intent(this, StudyActivity::class.java)
        startActivity(intent)
    }

    private fun playButtonClick() {
        //starting new activity
        val intent = Intent(this, PlayActivity::class.java)
        startActivity(intent)
    }

    private fun statisticsButtonClick() {
        //starting new activity
        val intent = Intent(this, StatisticsActivity::class.java)
        startActivity(intent)
    }
}
