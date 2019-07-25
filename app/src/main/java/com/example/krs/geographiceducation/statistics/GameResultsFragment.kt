package com.example.krs.geographiceducation.statistics

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.krs.geographiceducation.R
import com.example.krs.geographiceducation.common.helpers.UtilsAndHelpers
import com.example.krs.geographiceducation.model.database.GameResult
import kotlinx.android.synthetic.main.fragment_game_result.view.*

/**
 * Fragment that displays the results of a game
 */
class GameResultsFragment(gameResult: GameResult) : Fragment() {
    private val mGameResult = gameResult

    companion object {
        const val TOOLBAR_TITLE: String = "Result"

        fun newInstance(gameResult: GameResult): GameResultsFragment {
            return GameResultsFragment(gameResult)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View =
            inflater.inflate(R.layout.fragment_game_result, container, false)
        view.setBackgroundColor(ContextCompat.getColor(context!!, R.color.white))

        //setting toolbar
        val toolbar: Toolbar = view.findViewById(R.id.toolbar_local)
        toolbar.navigationIcon =
            ContextCompat.getDrawable(context!!, R.drawable.ic_arrow_back_white)
        toolbar.title = TOOLBAR_TITLE
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        toolbar.setTitleTextColor(Color.WHITE)
        toolbar.setNavigationOnClickListener { activity!!.onBackPressed() }

        UtilsAndHelpers.fillTextViewWithColoredText(
            activity!!,
            view.game_type_text_view,
            R.string.game_type,
            mGameResult.mGameName
        )
        UtilsAndHelpers.fillTextViewWithColoredText(
            activity!!,
            view.correct_answers_text_view,
            R.string.correct_answers,
            mGameResult.mCorrectAnswers
        )
        UtilsAndHelpers.fillTextViewWithColoredText(
            activity!!,
            view.game_duration_text_view,
            R.string.game_duration,
            mGameResult.mGameDuration
        )
        UtilsAndHelpers.fillTextViewWithColoredText(
            activity!!,
            view.game_date_text_view,
            R.string.game_date,
            mGameResult.mGameDate
        )

        return view
    }
}