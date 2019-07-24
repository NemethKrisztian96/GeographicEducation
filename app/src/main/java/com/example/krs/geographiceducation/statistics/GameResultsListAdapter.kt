package com.example.krs.geographiceducation.statistics

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.krs.geographiceducation.R
import com.example.krs.geographiceducation.common.helpers.UtilsAndHelpers
import com.example.krs.geographiceducation.model.database.GameResult
import kotlinx.android.synthetic.main.game_results_list_item.view.*

/**
 * An implementation of RecyclerView.Adapter meant for the listing minimal details about a game result in a RecyclerView
 */
class GameResultsListAdapter(activity: Context, clickListener: (GameResult) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val mActivity = activity
    var mGameResults: List<GameResult> = listOf()
    private val mClickListener = clickListener

    companion object {
        var isClickable: Boolean = true
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(mActivity, mGameResults[position], mClickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemLayoutView = LayoutInflater.from(parent.context)
            .inflate(R.layout.game_results_list_item, parent, false)

        return ViewHolder(itemLayoutView)
    }

    override fun getItemCount(): Int {
        return mGameResults.size
    }
}

/**
 * An implementation of RecyclerView.ViewHolder used to inflate the elements of the RecyclerView
 */
class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(context: Context, gameResult: GameResult, clickListener: (GameResult) -> Unit) {
        UtilsAndHelpers.fillTextViewWithColoredText(
            context as Activity,
            itemView.game_type_text_view,
            R.string.game_type_string,
            gameResult.mGameName
        )
        UtilsAndHelpers.fillTextViewWithColoredText(
            context,
            itemView.game_date_text_view,
            R.string.game_date,
            gameResult.mGameDate
        )
        UtilsAndHelpers.fillTextViewWithColoredText(
            context,
            itemView.correct_answers_text_view,
            R.string.correct_answers,
            gameResult.mCorrectAnswers
        )

        itemView.setOnClickListener {
            if (GameResultsListAdapter.isClickable) {
                clickListener(gameResult)
            }
        }
    }
}