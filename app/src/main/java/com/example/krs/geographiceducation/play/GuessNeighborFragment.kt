package com.example.krs.geographiceducation.play

import android.annotation.SuppressLint
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
import com.example.krs.geographiceducation.R
import com.example.krs.geographiceducation.common.GuessGameFragment
import com.example.krs.geographiceducation.common.UtilsAndHelpers
import com.example.krs.geographiceducation.logic.game.GameLogic
import com.example.krs.geographiceducation.model.database.GameResult
import com.example.krs.geographiceducation.model.database.SQLiteDBHelper
import com.example.krs.geographiceducation.statistics.GameResultsFragment
import com.example.krs.geographiceducation.study.StudyActivity
import kotlinx.android.synthetic.main.fragment_guess_the_capital.*
import kotlinx.android.synthetic.main.fragment_guess_the_capital.view.*
import java.text.SimpleDateFormat
import java.util.*

class GuessNeighborFragment(gameLogic: GameLogic) : GuessGameFragment() {
    private lateinit var mRegion: String
    private lateinit var mParent: PlayActivity
    private var mGameLogic: GameLogic = gameLogic

    companion object {
        const val TAG = "GuessNeighborFragment"
        const val GAME_TYPE = "Guess the neighbor"

        fun newInstance(gameLogic: GameLogic): GuessNeighborFragment {
            return GuessNeighborFragment(gameLogic)
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
            inflater.inflate(R.layout.fragment_guess_the_neighbor, container, false)
        view.setBackgroundColor(ContextCompat.getColor(context!!, R.color.white))

        //setting toolbar
        val toolbar: Toolbar = view.findViewById(R.id.toolbar_local)
        toolbar.navigationIcon =
            ContextCompat.getDrawable(mParent, R.drawable.ic_arrow_back_white)
        toolbar.title = StudyActivity.TOOLBAR_TITLE
        mParent.setSupportActionBar(toolbar)

        toolbar.setTitleTextColor(Color.WHITE)
        toolbar.setNavigationOnClickListener { navigationOnClickListener() }

        val answerOptions = mGameLogic.getGuessNeighborData()

        //filling game layout buttons and country name
        view.country_name.text = mGameLogic.mCurrentCountry.mName
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

    override fun onDetach() {
        super.onDetach()

        //save result to db
        if (mGameLogic.isLastQuestion()) {
            SQLiteDBHelper(mParent, null).addResult(
                GameResult(
                    GAME_TYPE,
                    mGameLogic.getCorrectAnswerCount(),
                    UtilsAndHelpers.transformGameDuration(mGameLogic.getGameDurationMilliseconds()),
                    SimpleDateFormat("yyyy-MM-dd ").format(Date())
                )
            )
        }
    }

    private fun answerButtonClick(view: Button) {
        //preventing multiple clicks
        button_answer1.isClickable = false
        button_answer2.isClickable = false
        button_answer3.isClickable = false
        button_answer4.isClickable = false

        if (mGameLogic.isCorrectNeighbor(view.text.toString())) {
            view.setBackgroundColor(ContextCompat.getColor(context!!, R.color.correct_answer))
        } else {
            view.setBackgroundColor(ContextCompat.getColor(context!!, R.color.incorrect_answer))
        }
        val timerTask = object : TimerTask() {
            @SuppressLint("SimpleDateFormat")
            override fun run() {
                if (mGameLogic.isLastQuestion()) {
                    mParent.openFragment(
                        GameResultsFragment.newInstance(
                            GameResult(
                                GAME_TYPE,
                                mGameLogic.getCorrectAnswerCount(),
                                UtilsAndHelpers.transformGameDuration(mGameLogic.getGameDurationMilliseconds()),
                                SimpleDateFormat("yyyy-MM-dd ").format(Date())
                            )
                        )
                    )
                } else {
                    mParent.openFragment(
                        newInstance(
                            mGameLogic
                        )
                    )
                }
            }
        }
        Timer().schedule(timerTask, 1000)

    }

    override fun setRegion(regionName: String, parent: Activity) {
        if (parent is PlayActivity) {
            mParent = parent
        }
        mRegion = regionName
        UtilsAndHelpers.getCountriesDataWithRetrofit(mParent, mRegion, mParent.mCountries, null, this, mParent)
    }

    override fun setNumberOfQuestions(numberOfQuestions: Int) {
        mGameLogic.mNumberOfQuestions = numberOfQuestions
    }

    private fun navigationOnClickListener() {
        //ask for confirmation
        AlertDialog.Builder(mParent)
            .setTitle("Exit")
            .setMessage("Do you really want to leave the game?")
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton(R.string.yes) { dialog, whichButton ->
                //save current game results
                SQLiteDBHelper(mParent, null).addResult(
                    GameResult(
                        GAME_TYPE,
                        mGameLogic.getCorrectAnswerCount(),
                        UtilsAndHelpers.transformGameDuration(mGameLogic.getGameDurationMilliseconds()),
                        SimpleDateFormat("yyyy-MM-dd ").format(Date())
                    )
                )
                //go back to homepage
                mParent.onBackPressed()
            }
            .setNegativeButton(R.string.no, null)
            .show()
    }
}