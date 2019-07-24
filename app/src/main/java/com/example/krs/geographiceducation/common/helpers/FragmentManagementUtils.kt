package com.example.krs.geographiceducation.common.helpers

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.krs.geographiceducation.study.StudyActivity

/**
 * Class meant to ease fragment management -- abandoned
 */
class FragmentManagementUtils {
    companion object {
        private const val BACK_STACK_ROOT_TAG = "root_fragment"

        fun onTabSelected(activity: Activity, containerResourceId: Int, fragment: Fragment) {
            if (activity is StudyActivity) {
                // Pop off everything up to and including the current tab
                val fragmentManager = activity.supportFragmentManager
                fragmentManager.popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE)

                // Add the new tab fragment
                fragmentManager.beginTransaction()
                    .replace(containerResourceId, fragment)
                    .addToBackStack(BACK_STACK_ROOT_TAG)
                    .commit()
            }
        }

        /**
         * Add a fragment on top of the current tab
         */
        fun addFragmentOnTop(activity: Activity, containerResourceId: Int, fragment: Fragment) {
            if (activity is StudyActivity) {
                activity.supportFragmentManager
                    .beginTransaction()
                    .replace(containerResourceId, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
}