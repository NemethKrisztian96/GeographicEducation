package com.example.krs.geographiceducation.play

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.krs.geographiceducation.R
import com.example.krs.geographiceducation.common.GameLogic
import com.example.krs.geographiceducation.common.UtilsAndHelpers
import com.example.krs.geographiceducation.study.StudyActivity
import kotlinx.android.synthetic.main.fragment_guess_the_capital.view.*
import java.util.*


class GuessCapitalFragment(gameLogic: GameLogic? = null) : Fragment() {
    private lateinit var mRegion: String
    private lateinit var mParent: PlayActivity
    private var mGameLogic: GameLogic? = gameLogic

    companion object {
        const val TAG = "GuessCapitalFragment"

        fun newInstance(gameLogic: GameLogic? = null): GuessCapitalFragment {
            return GuessCapitalFragment(gameLogic)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is PlayActivity) {
            mParent = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View =
            inflater.inflate(com.example.krs.geographiceducation.R.layout.fragment_guess_the_capital, container, false)
        view.setBackgroundColor(ContextCompat.getColor(context!!, com.example.krs.geographiceducation.R.color.white))

        //setting toolbar
        var toolbar: Toolbar = view.findViewById(com.example.krs.geographiceducation.R.id.toolbar_local)
        toolbar.navigationIcon =
            ContextCompat.getDrawable(mParent, com.example.krs.geographiceducation.R.drawable.ic_arrow_back_white)
        toolbar.title = StudyActivity.TOOLBAR_TITLE
        mParent.setSupportActionBar(toolbar)

        toolbar.setTitleTextColor(Color.WHITE)
        toolbar.setNavigationOnClickListener { _ -> navigationOnClickListener() }

        //generating game
        if (mGameLogic == null) {
            mGameLogic = GameLogic(mParent.mCountries)
        }
        var answerOptions = mGameLogic!!.getGuessCapitalData()

        //filling game layout buttons and country name
        view.country_name.text = mGameLogic!!.mCurrentCountry.mName
        view.button_answer1.text = answerOptions[0]
        view.button_answer2.text = answerOptions[1]
        view.button_answer3.text = answerOptions[2]
        view.button_answer4.text = answerOptions[3]

        view.button_answer1.setOnClickListener {
            answerButtonClick(it as Button)
        }

        view.button_answer2.setOnClickListener {
            answerButtonClick(it as Button)
        }

        view.button_answer3.setOnClickListener {
            answerButtonClick(it as Button)
        }

        view.button_answer4.setOnClickListener {
            answerButtonClick(it as Button)
        }

        return view
    }

    private fun answerButtonClick(view: Button) {
        if (mGameLogic!!.isCorrectCapital(view.text.toString())) {
            view.setBackgroundColor(ContextCompat.getColor(context!!, R.color.correct_answer))
        } else {
            view.setBackgroundColor(ContextCompat.getColor(context!!, R.color.incorrect_answer))
        }
        var timertask = object : TimerTask() {
            override fun run() {
                mParent.openFragment(GuessCapitalFragment(mGameLogic))
            }
        }
        Timer().schedule(timertask, 1000)
    }

    fun setRegion(regionName: String, parent: Activity) {
        if (parent is PlayActivity) {
            mParent = parent
        }
        mRegion = regionName
        UtilsAndHelpers.getCountriesDataWithRetrofit(mParent, mRegion, mParent.mCountries, null, this, mParent)
    }

    private fun navigationOnClickListener() {
        //ask for confirmation
        AlertDialog.Builder(mParent)
            .setTitle("Exit")
            .setMessage("Do you really want to go back to the homepage?")
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton(
                android.R.string.yes
            ) { dialog, whichButton ->
                //go back to homepage
                mParent.finish()
            }
            .setNegativeButton(android.R.string.no, null)
            .show()
    }
}