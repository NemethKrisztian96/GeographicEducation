package com.example.krs.geographiceducation.logic.game

import com.example.krs.geographiceducation.model.Country
import java.util.*

/**
 * Class that handles all the tasks required for the games
 */
class GameLogic(countries: List<Country>) {
    private var mCountries: List<Country> = countries
    lateinit var mCurrentCountry: Country
    var mNumberOfQuestions: Int = 15
    private var mPreviousCountries: MutableList<Country> = mutableListOf()
    private val mRandom = Random()
    private var mStartTime: Long = 0
    private var mCorrectAnswers: Int = 0

    private val NUMBER_OF_ANSWER_OPTIONS: Int = 4
    private val HAS_NO_CAPITAL: String = "Has no capital"
    private val HAS_NO_NEIGHBOR: String = "Has no neighbor"

    /**
     * Generates data for the Guess the capital game
     * @return list of answer options formed by capital names
     */
    fun getGuessCapitalData(): List<String> {
        mCurrentCountry = getRandomNewCountry()
        mPreviousCountries.add(mCurrentCountry)

        //building a list with possible answers
        val answerOptions = mutableListOf<String>()
        if (mCurrentCountry.mCapital.length > 2) {
            answerOptions.add(mCurrentCountry.mCapital)
        } else {
            answerOptions.add(HAS_NO_CAPITAL)
        }
        while (answerOptions.size < NUMBER_OF_ANSWER_OPTIONS) {
            val wrongCapital = getWrongCapital()
            if (!answerOptions.contains(wrongCapital)) {
                answerOptions.add(wrongCapital)
            }
        }

        //setting game start time if this is the first question
        if (mPreviousCountries.size == 1) {
            mStartTime = System.currentTimeMillis()
        }

        return answerOptions.shuffled()
    }

    /**
     * Generates data for the Guess the neighbor game
     * @return list of answer options formed by country names
     */
    fun getGuessNeighborData(): List<String> {
        mCurrentCountry = getRandomNewCountry()
        mCurrentCountry.initializeMembersFromCountryList(mCountries)
        mPreviousCountries.add(mCurrentCountry)

        //building a list with possible answers
        val answerOptions = mutableListOf<String>()
        if (!mCurrentCountry.mNeighbors.isNullOrEmpty()) {
            answerOptions.add(mCurrentCountry.mNeighbors[generateRandomInRange(mCurrentCountry.mNeighbors.size)].mName)
        } else {
            answerOptions.add(HAS_NO_NEIGHBOR)
        }
        while (answerOptions.size < NUMBER_OF_ANSWER_OPTIONS) {
            val wrongNeighbor = getWrongNeighbor()
            if (!answerOptions.contains(wrongNeighbor)) {
                answerOptions.add(wrongNeighbor)
            }
        }

        //setting game start time if this is the first question
        if (mPreviousCountries.size == 1) {
            mStartTime = System.currentTimeMillis()
        }

        return answerOptions.shuffled()
    }

    /**
     * Generates data for the Guess the flag game
     * @return list of answer options formed by country alpha2codes
     */
    fun getGuessFlagData(): List<String> {
        mCurrentCountry = getRandomNewCountry()
        //mCurrentCountry.initializeMembersFromCountryList(mCountries)
        mPreviousCountries.add(mCurrentCountry)

        //building a list with possible answers
        val answerOptions = mutableListOf<String>()
        answerOptions.add(mCurrentCountry.mAlpha2code)

        while (answerOptions.size < NUMBER_OF_ANSWER_OPTIONS) {
            val wrongFlag = getWrongFlag()
            if (!answerOptions.contains(wrongFlag)) {
                answerOptions.add(wrongFlag)
            }
        }

        //setting game start time if this is the first question
        if (mPreviousCountries.size == 1) {
            mStartTime = System.currentTimeMillis()
        }

        return answerOptions.shuffled()
    }

    /**
     * Selects randomly a country that hasn't been used before in this game
     */
    private fun getRandomNewCountry(): Country {
        //finding a country that hasn't been selected yet
        var index = generateRandomInRange(mCountries.size)
        while (mPreviousCountries.contains(mCountries[index])) {
            index = generateRandomInRange(mCountries.size)
        }
        return mCountries[index]
    }

    /**
     * Returns true if this is the last question of the game
     */
    fun isLastQuestion(): Boolean {
        return mNumberOfQuestions - mPreviousCountries.size == 0
    }

    /**
     * Returns the number of correctly answered question in the game
     */
    fun getCorrectAnswerCount(): Int {
        return mCorrectAnswers
    }

    /**
     * Returns the game duration in milliseconds
     */
    fun getGameDurationMilliseconds(): Long {
        return (System.currentTimeMillis() - mStartTime)
    }

    /**
     * Returns whether the given answered capital is correct or not
     */
    fun isCorrectCapital(answer: String): Boolean {
        if (mCurrentCountry.mCapital == answer || (answer == HAS_NO_CAPITAL && mCurrentCountry.mCapital == "")) {
            mCorrectAnswers++
            return true
        }
        return false
    }

    /**
     * Returns whether the given answered neighbor is correct or not
     */
    fun isCorrectNeighbor(answer: String): Boolean {
        if (answer == HAS_NO_NEIGHBOR && mCurrentCountry.mNeighbors.isNullOrEmpty()) {
            mCorrectAnswers++
            return true
        }
        mCurrentCountry.mNeighbors.forEach {
            if (it.mName == answer) {
                mCorrectAnswers++
                return true
            }
        }
        return false
    }

    /**
     * Returns whether the given answered flag is correct or not
     */
    fun isCorrectFlag(answer: String): Boolean {
        if (mCurrentCountry.mAlpha2code == answer) {
            mCorrectAnswers++
            return true
        }
        return false
    }

    /**
     * Generates a number in the range [0, upperBound)
     * @return the generated Int
     */
    private fun generateRandomInRange(upperBound: Int): Int {
        return mRandom.nextInt(upperBound)
    }

    /**
     * Chooses a random capital from the countries list
     */
    private fun getWrongCapital(): String {
        val country = mCountries[generateRandomInRange(mCountries.size)]
        return if (country.mCapital.length < 2) {
            HAS_NO_CAPITAL
        } else {
            country.mCapital
        }
    }

    /**
     * Chooses a random country name in the countries list
     */
    private fun getWrongNeighbor(): String {
        val country = mCountries[generateRandomInRange(mCountries.size)]
        return if (country == mCurrentCountry || mCurrentCountry.mNeighbors.contains(country)) {
            HAS_NO_NEIGHBOR
        } else {
            country.mName
        }
    }

    /**
     * Chooses a random country's alpha2code from the countries list
     */
    private fun getWrongFlag(): String {
        return mCountries[generateRandomInRange(mCountries.size)].mAlpha2code
    }
}