package com.example.krs.geographiceducation

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.krs.geographiceducation.play.PlayActivity
import com.example.krs.geographiceducation.statistics.StatisticsActivity
import com.example.krs.geographiceducation.study.StudyActivity

/**
 * Activity that allows the user to choose which of the three main functions wants to use
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun studyButtonClick(view: View) {
        //starting new activity
        val intent = Intent(this, StudyActivity::class.java)
        startActivity(intent)
    }

    fun playButtonClick(view: View) {
        //starting new activity
        val intent = Intent(this, PlayActivity::class.java)
        startActivity(intent)
    }

    fun statisticsButtonClick(view: View) {
        //starting new activity
        val intent = Intent(this, StatisticsActivity::class.java)
        startActivity(intent)
    }
}
