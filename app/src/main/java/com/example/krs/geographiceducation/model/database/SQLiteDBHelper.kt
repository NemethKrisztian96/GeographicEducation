package com.example.krs.geographiceducation.model.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteDBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 2
        const val DATABASE_NAME = "geo_ed_database"
        const val RESULT_TABLE_NAME = "Results"
        const val RESULT_COLUMN_ID = "_id"
        const val RESULT_COLUMN_GAME_NAME = "game_name"
        const val RESULT_COLUMN_CORRECT_ANSWER = "correct_answers"
        const val RESULT_COLUMN_GAME_DURATION = "game_duration"
        const val RESULT_COLUMN_GAME_DATE = "game_date"
    }


    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        sqLiteDatabase.execSQL(
            "CREATE TABLE $RESULT_TABLE_NAME ($RESULT_COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$RESULT_COLUMN_GAME_NAME TEXT, $RESULT_COLUMN_CORRECT_ANSWER TEXT, " +
                    "$RESULT_COLUMN_GAME_DURATION TEXT, $RESULT_COLUMN_GAME_DATE TEXT)"
        )
        //insertDummyData()
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, p2: Int) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS $RESULT_TABLE_NAME")
        onCreate(sqLiteDatabase)
    }

    fun getAllResults(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $RESULT_TABLE_NAME", null)
    }

    fun addResult(result: GameResult) {
        val values = ContentValues()
        values.put(RESULT_COLUMN_GAME_NAME, result.mGameName)
        values.put(RESULT_COLUMN_CORRECT_ANSWER, result.mCorrectAnswers)
        values.put(RESULT_COLUMN_GAME_DURATION, result.mGameDuration)
        values.put(RESULT_COLUMN_GAME_DATE, result.mGameDate)
        val db = writableDatabase
        db.insert(RESULT_TABLE_NAME, null, values)
        db.close()
    }

    fun insertDummyData() {
        val values = ContentValues()
        values.put(RESULT_COLUMN_GAME_NAME, "Dummy game")
        values.put(RESULT_COLUMN_CORRECT_ANSWER, "10/10")
        values.put(RESULT_COLUMN_GAME_DURATION, "00:01")
        values.put(RESULT_COLUMN_GAME_DATE, "2019-09-09 25:61")
        val db = writableDatabase
        db.insert(RESULT_TABLE_NAME, null, values)
        db.close()
    }
}
