package com.example.krs.geographiceducation.play

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.krs.geographiceducation.R
import com.example.krs.geographiceducation.logic.NavigationHelpers

class PlayActivity : AppCompatActivity() {
    companion object {
        const val TOOLBAR_TITLE: String = "Play"
        private val TAG: String = "PlayActivity"
    }

    private var mOpenFragments: MutableList<Fragment> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        //setting toolbar
        var toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_white)
        toolbar.title = TOOLBAR_TITLE
        toolbar.setTitleTextColor(Color.WHITE)
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            NavigationHelpers.navigationOnClickListener(this, mOpenFragments, supportFragmentManager)
        }


    }

}