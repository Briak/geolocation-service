package com.kristinaefros.challenge.presentation.common.navigation

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import com.kristinaefros.challenge.R
import com.kristinaefros.challenge.presentation.start.StartFragment
import com.kristinaefros.challenge.presentation.places.PlacesFragment


class Navigator(
    private val activity: AppCompatActivity
) {
    private var primaryScreenTag: String? = null

    fun openStartScreen() = openPrimary(StartFragment.newInstance())
    fun openPlacesScreen() = openPrimary(PlacesFragment.newInstance())

    private fun openPrimary(fragment: Fragment) {
        if (primaryScreenTag == fragment::class.java.simpleName) return
        primaryScreenTag = fragment::class.java.simpleName
        val manager = activity.supportFragmentManager
        manager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

        val currentFragment = manager.findFragmentById(R.id.content_main)
        val existingFragment = manager.findFragmentByTag(primaryScreenTag)
        val createdFragment = if (existingFragment == null) fragment else null

        manager.commit {
            if (currentFragment != null) {
                detach(currentFragment)
            }
            if (existingFragment != null) {
                attach(existingFragment)
            }
            if (createdFragment != null) {
                add(R.id.content_main, createdFragment, primaryScreenTag)
            }
        }
    }

    private fun openNested(fragment: Fragment) {
        val manager = activity.supportFragmentManager
        val currentFragment = manager.findFragmentById(R.id.content_main)
        manager.commit {
            if (currentFragment != null) setMaxLifecycle(currentFragment, Lifecycle.State.STARTED)
            add(R.id.content_main, fragment)
            addToBackStack(fragment::class.java.simpleName)
        }
    }

    fun findTopFragment(): Fragment? {
        val manager = activity.supportFragmentManager
        return manager.findFragmentById(R.id.content_main)
    }

    fun findLastPrimaryFragment(): Fragment? = activity.supportFragmentManager.findFragmentByTag(primaryScreenTag)

    fun back() {
        val manager = activity.supportFragmentManager
        manager.popBackStack()
    }

}