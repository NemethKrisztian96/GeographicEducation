package com.example.krs.geographiceducation.common

import android.app.Activity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment

/**
 * An abstract class for unified "Guess games" in the Play functionality
 */
abstract class GuessGameFragment : Fragment() {
    lateinit var mToolbar: Toolbar

    /**
     * Sets the given region to the fragments country list
     */
    abstract fun setRegion(regionName: String, parent: Activity)

    /**
     * Sets the number of questions the game will contain
     */
    abstract fun setNumberOfQuestions(numberOfQuestions: Int)

    /**
     * Prompts the user to confirm the intention to leave the game and performs that when receives confirmation
     */
    abstract fun navigationOnClickListener()
}