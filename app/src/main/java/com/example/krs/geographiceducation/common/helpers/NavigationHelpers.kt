package com.example.krs.geographiceducation.common.helpers

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * Class meant to ease the usage of navigation bars by offering methods for navigation click listener and fragment removal -- partially used
 */
class NavigationHelpers {
    companion object {
        fun navigationOnClickListener(
            activity: Activity,
            openFragments: MutableList<Fragment>,
            supportFragmentManager: FragmentManager
        ) {
            if (openFragments.size > 1) {
                //removing all fragments except the first (which should be CountryListFragment)
                for (i in 0 until openFragments.size - 1) {
                    removeLastAddedFragment(
                        openFragments,
                        supportFragmentManager
                    )
                }
            } else {
                activity.onBackPressed()
            }
        }

        private fun removeLastAddedFragment(
            openFragments: MutableList<Fragment>,
            supportFragmentManager: FragmentManager
        ) {
            val fragment = openFragments.last()
            supportFragmentManager.beginTransaction()
                .remove(fragment)
                .commit()
            openFragments.remove(fragment)
        }
    }
}