package com.kerencev.mynasa.view.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kerencev.mynasa.R
import com.kerencev.mynasa.model.helpers.SPreference
import com.kerencev.mynasa.view.settings.SettingsFragment

const val THEME_MY_NASA = 0
const val THEME_SPACE = 1

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCurrentTheme()
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MainFragment())
                .commitAllowingStateLoss()
        }
        setBottomNavigationClicks()
    }

    private fun setBottomNavigationClicks() {
        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_photo_of_the_day -> navigateTo(MainFragment())
                R.id.action_settings -> navigateTo(SettingsFragment())
            }
            true
        }
    }

    private fun navigateTo(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commitAllowingStateLoss()
    }

    private fun setCurrentTheme() {
        when (val index = SPreference.getCurrentTheme(this)) {
            0 -> {
                setTheme(R.style.Theme_MyNasa)
            }
            1 -> {
                setTheme(R.style.ThemeSpace)
            }
        }
    }
}