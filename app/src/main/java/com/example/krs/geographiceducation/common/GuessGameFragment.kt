package com.example.krs.geographiceducation.common

import android.app.Activity
import androidx.fragment.app.Fragment

abstract class GuessGameFragment : Fragment() {
    abstract fun setRegion(regionName: String, parent: Activity)
    abstract fun setNumberOfQuestions(numberOfQuestions: Int)
}