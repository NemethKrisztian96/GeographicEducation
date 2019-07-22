package com.example.krs.geographiceducation.common

import com.example.krs.geographiceducation.model.Country
import java.util.*

class GameLogic(countries: List<Country>) {
    var mCountries: List<Country> = countries
    lateinit var mCurrentCountry: Country
    private var mPreviousCountries: MutableList<Country> = mutableListOf()
    private val mRandom = Random()

    private val NUMBER_OF_ANSWER_OPTIONS: Int = 4

    /**
     * Generates data for the Guess the capital game
     * @return list of answer options formed by capital names
     */
    fun getGuessCapitalData(): List<String> {
        //finding a country that hasn't been selected yet
        var index = generateRandomInRange(0, mCountries.size)
        while (mPreviousCountries.contains(mCountries[index])) {
            index = generateRandomInRange(0, mCountries.size)
        }

        mCurrentCountry = mCountries[index]
        mCurrentCountry.initializeMembersFromCountryList(mCountries)
        mPreviousCountries.add(mCurrentCountry)

        //building a list with possible answers
        var answerOptions = mutableListOf<String>()
        answerOptions.add(mCurrentCountry.mCapital)
        while (answerOptions.size < NUMBER_OF_ANSWER_OPTIONS) {
            var wrongCapital = getWrongCapital()
            if (wrongCapital.isNotEmpty() && !answerOptions.contains(wrongCapital)) {
                answerOptions.add(wrongCapital)
            }
        }

        return answerOptions.shuffled()
    }

    fun isCorrectCapital(answer: String): Boolean {
        return mCurrentCountry.mCapital == answer
    }

    /**
     * Generates a number in the range [lowerBound, upperBound)
     * @return the generated Int
     */
    private fun generateRandomInRange(lowerBound: Int, upperBound: Int): Int {
        //return (lowerBound until upperBound).shuffled().first()
        return mRandom.nextInt(upperBound)
    }

    private fun getWrongCapital(): String {
        return mCountries[generateRandomInRange(0, mCountries.size)].mCapital
    }
}