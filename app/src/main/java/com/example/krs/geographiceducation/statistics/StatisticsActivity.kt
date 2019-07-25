package com.example.krs.geographiceducation.statistics

import android.database.CursorIndexOutOfBoundsException
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.krs.geographiceducation.R
import com.example.krs.geographiceducation.model.database.GameResult
import com.example.krs.geographiceducation.model.database.SQLiteDBHelper
import kotlinx.android.synthetic.main.activity_statistics.*

/**
 * Activity that is responsible of the "Statistics" functionality
 */
class StatisticsActivity : AppCompatActivity() {
    private lateinit var mRecyclerView: RecyclerView

    companion object {
        const val TOOLBAR_TITLE: String = "Statistics"
        private const val TAG: String = "Statistics Activity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        //setting toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar_local)
        toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_white)
        toolbar.title = TOOLBAR_TITLE
        toolbar.setTitleTextColor(Color.WHITE)
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener { onBackPressed() }

        //setting recyclerView
        mRecyclerView = findViewById(R.id.results_recycler_view)
        mRecyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = GameResultsListAdapter(this) { gameResult: GameResult ->
            openGameResultFragment(GameResultsFragment.newInstance(gameResult))
        }
        adapter.mGameResults = getGameResultsFromDatabase()
        //adapter.notifyDataSetChanged()
        mRecyclerView.adapter = adapter
        //mRecyclerView.addItemDecoration(ItemDividerDecoration(this))

        if (adapter.mGameResults.isNullOrEmpty()) {
            mRecyclerView.visibility = View.GONE
            error_message.visibility = View.VISIBLE
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.fragments.size > 0) {
            supportFragmentManager.beginTransaction().remove(supportFragmentManager.fragments.last()).commit()
            GameResultsListAdapter.isClickable = true
        } else {
            super.onBackPressed()
        }
    }

    /**
     * Opens the given fragment
     */
    private fun openGameResultFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .add(R.id.activity_statistics, fragment)
            .commit()
        GameResultsListAdapter.isClickable = false
    }

    /**
     * Gets all data from the database and returns it as a list
     */
    private fun getGameResultsFromDatabase(): List<GameResult> {
        val gameResults = mutableListOf<GameResult>()
        val dbHandler = SQLiteDBHelper(this, null)
        val cursor = dbHandler.getAllResults()
        try {
            cursor!!.moveToFirst()

            gameResults.add(
                GameResult(
                    cursor.getString(cursor.getColumnIndex(SQLiteDBHelper.RESULT_COLUMN_GAME_NAME)),
                    cursor.getString(cursor.getColumnIndex(SQLiteDBHelper.RESULT_COLUMN_CORRECT_ANSWER)),
                    cursor.getString(cursor.getColumnIndex(SQLiteDBHelper.RESULT_COLUMN_GAME_DURATION)),
                    cursor.getString(cursor.getColumnIndex(SQLiteDBHelper.RESULT_COLUMN_GAME_DATE))
                )
            )
            while (cursor.moveToNext()) {
                gameResults.add(
                    GameResult(
                        cursor.getString(cursor.getColumnIndex(SQLiteDBHelper.RESULT_COLUMN_GAME_NAME)),
                        cursor.getString(cursor.getColumnIndex(SQLiteDBHelper.RESULT_COLUMN_CORRECT_ANSWER)),
                        cursor.getString(cursor.getColumnIndex(SQLiteDBHelper.RESULT_COLUMN_GAME_DURATION)),
                        cursor.getString(cursor.getColumnIndex(SQLiteDBHelper.RESULT_COLUMN_GAME_DATE))
                    )
                )
            }
        } catch (ex: CursorIndexOutOfBoundsException) {

        } finally {
            cursor?.close()
        }

        return gameResults
    }
}