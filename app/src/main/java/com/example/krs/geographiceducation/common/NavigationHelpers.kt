package com.example.krs.geographiceducation.common

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

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