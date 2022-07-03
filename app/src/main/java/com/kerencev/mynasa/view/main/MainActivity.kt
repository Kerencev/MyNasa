package com.kerencev.mynasa.view.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kerencev.mynasa.R
import com.kerencev.mynasa.model.helpers.SPreference

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