package com.example.krs.geographiceducation.play

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.krs.geographiceducation.MainActivity
import com.example.krs.geographiceducation.R
import com.example.krs.geographiceducation.common.GuessGameFragment
import com.example.krs.geographiceducation.common.helpers.UtilsAndHelpers
import com.example.krs.geographiceducation.logic.game.GameLogic
import com.example.krs.geographiceducation.model.database.GameResult
import com.example.krs.geographiceducation.model.database.SQLiteDBHelper
import com.example.krs.geographiceducation.statistics.GameResultsFragment
import kotlinx.android.synthetic.main.fragment_guess_the_flag.*
import kotlinx.android.synthetic.main.fragment_guess_the_flag.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Fragment responsible for the "Guess the flag" game
 */
class GuessFlagFragment(gameLogic: GameLogic) : GuessGameFragment() {
    private lateinit var mRegion: String
    private lateinit var mParent: PlayActivity
    private var mGameLogic: GameLogic = gameLogic

    companion object {
        const val TAG = "GuessFlagFragment"
        const val GAME_TYPE = "Guess the flag"

        fun newInstance(gameLogic: GameLogic): GuessFlagFragment {
            return GuessFlagFragment(gameLogic)
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
            inflater.inflate(R.layout.fragment_guess_the_flag, container, false)
        view.setBackgroundColor(ContextCompat.getColor(context!!, R.color.white))

        //setting toolbar
        mToolbar = view.findViewById(R.id.toolbar_local)
        mToolbar.navigationIcon =
            ContextCompat.getDrawable(mParent, R.drawable.ic_arrow_back_white)
        mToolbar.title = PlayActivity.TOOLBAR_TITLE
        mParent.setSupportActionBar(mToolbar)

        mToolbar.setTitleTextColor(Color.WHITE)
        mToolbar.setNavigationOnClickListener { navigationOnClickListener() }

        val answerOptions = mGameLogic.getGuessFlagData()

        //filling game layout imageViews and country name
        view.country_name.text = mGameLogic.mCurrentCountry.mName
        view.image_answer1.contentDescription = answerOptions[0]
        UtilsAndHelpers.getImageWithGlide(
            mParent,
            UtilsAndHelpers.FLAG_BASE_URL + answerOptions[0] + "/flat/64.png",
            UtilsAndHelpers.getGlideRequestOptionsForBiggerImg(),
            view.image_answer1
        )

        view.image_answer2.contentDescription = answerOptions[1]
        UtilsAndHelpers.getImageWithGlide(
            mParent,
            UtilsAndHelpers.FLAG_BASE_URL + answerOptions[1] + "/flat/64.png",
            UtilsAndHelpers.getGlideRequestOptionsForBiggerImg(),
            view.image_answer2
        )

        view.image_answer3.contentDescription = answerOptions[2]
        UtilsAndHelpers.getImageWithGlide(
            mParent,
            UtilsAndHelpers.FLAG_BASE_URL + answerOptions[2] + "/flat/64.png",
            UtilsAndHelpers.getGlideRequestOptionsForBiggerImg(),
            view.image_answer3
        )

        view.image_answer4.contentDescription = answerOptions[3]
        UtilsAndHelpers.getImageWithGlide(
            mParent,
            UtilsAndHelpers.FLAG_BASE_URL + answerOptions[3] + "/flat/64.png",
            UtilsAndHelpers.getGlideRequestOptionsForBiggerImg(),
            view.image_answer4
        )

        view.image_answer1.setOnClickListener {
            answerImageClick(it as ImageView)
        }

        view.image_answer2.setOnClickListener {
            answerImageClick(it as ImageView)
        }

        view.image_answer3.setOnClickListener {
            answerImageClick(it as ImageView)
        }

        view.image_answer4.setOnClickListener {
            answerImageClick(it as ImageView)
        }

        return view
    }

    /**
     * Handles and classifies the given answer, opening the next question afterwards
     */
    private fun answerImageClick(view: ImageView) {
        //preventing multiple clicks
        image_answer1.isClickable = false
        image_answer2.isClickable = false
        image_answer3.isClickable = false
        image_answer4.isClickable = false

        if (mGameLogic.isCorrectFlag(view.contentDescription.toString())) {
            view.setBackgroundColor(ContextCompat.getColor(context!!, R.color.correct_answer))
        } else {
            view.setBackgroundColor(ContextCompat.getColor(context!!, R.color.incorrect_answer))
        }
        val timerTask = object : TimerTask() {
            @SuppressLint("SimpleDateFormat")
            override fun run() {
                if (mGameLogic.isLastQuestion()) {
                    SQLiteDBHelper(mParent, null).addResult(
                        GameResult(
                            GAME_TYPE,
                            "${mGameLogic.getCorrectAnswerCount()}/${mGameLogic.mNumberOfQuestions}",
                            UtilsAndHelpers.transformGameDuration(mGameLogic.getGameDurationMilliseconds()),
                            SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date())
                        )
                    )

                    mParent.openFragment(
                        GameResultsFragment.newInstance(
                            GameResult(
                                GAME_TYPE,
                                "${mGameLogic.getCorrectAnswerCount()}/${mGameLogic.mNumberOfQuestions}",
                                UtilsAndHelpers.transformGameDuration(mGameLogic.getGameDurationMilliseconds()),
                                SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date())
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

        if (UtilsAndHelpers.dataSaverIsChecked) {
            mParent.mCountries = MainActivity.allCountries.filter {
                it.mRegion == mRegion.capitalize()
            }.toMutableList()
            mGameLogic.mCountries = mParent.mCountries
        } else {
            UtilsAndHelpers.getCountriesDataWithRetrofit(mParent, mRegion, mParent.mCountries, null, this, mParent)
        }
    }

    override fun setNumberOfQuestions(numberOfQuestions: Int) {
        mGameLogic.mNumberOfQuestions = numberOfQuestions
    }

    /**
     * Prompts the user to confirm the intention to leave the game and performs that when receives confirmation
     */
    @SuppressLint("SimpleDateFormat")
    override fun navigationOnClickListener() {
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
                        "${mGameLogic.getCorrectAnswerCount()}/${mGameLogic.mNumberOfQuestions}",
                        UtilsAndHelpers.transformGameDuration(mGameLogic.getGameDurationMilliseconds()),
                        SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date())
                    )
                )
                //go back to homepage
                activity?.finish()
                startActivity(activity?.intent)
            }
            .setNegativeButton(R.string.no, null)
            .show()
    }
}