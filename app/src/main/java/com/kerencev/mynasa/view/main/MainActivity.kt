package com.kerencev.mynasa.view.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kerencev.mynasa.R
import com.kerencev.mynasa.databinding.ActivityMainBinding
import com.kerencev.mynasa.model.helpers.SPreference
import com.kerencev.mynasa.view.earth.ViewPagerEarthFragment
import com.kerencev.mynasa.view.mars.MarsFragment
import com.kerencev.mynasa.view.recycler.RecyclerFragment
import com.kerencev.mynasa.view.photooftheday.ViewPagerPhotoOfTheDayFragment
import com.kerencev.mynasa.view.settings.SettingsFragment

class MainActivity : AppCompatActivity(), BottomNavigationHandler {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCurrentTheme()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ViewPagerPhotoOfTheDayFragment())
                .commitAllowingStateLoss()
        }
        setBottomNavigationClicks()
    }

    private fun setBottomNavigationClicks() {
        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_photo_of_the_day -> navigateTo(
                    ViewPagerPhotoOfTheDayFragment(),
                    "ViewPagerPhotoOfTheDayFragment"
                )
                R.id.action_settings -> navigateTo(SettingsFragment(), "")
                R.id.action_earth -> navigateTo(ViewPagerEarthFragment(), "")
                R.id.action_mars -> navigateTo(MarsFragment(), "")
                R.id.action_moon -> navigateTo(RecyclerFragment(), "")
            }
            true
        }
    }

    private fun navigateTo(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment, tag)
            .commitAllowingStateLoss()
    }

    private fun setCurrentTheme() {
        when (SPreference.getCurrentTheme(this)) {
            0 -> {
                setTheme(R.style.Theme_MyNasa)
            }
            1 -> {
                setTheme(R.style.ThemeSpace)
            }
        }
    }

    override fun isShowBottomNavigation(isShow: Boolean) {
        when (isShow) {
            true -> binding.bottomNavigation.visibility = View.VISIBLE
            false -> binding.bottomNavigation.visibility = View.GONE
        }
    }

    override fun isShowSystemBar(isShow: Boolean) {
        when (isShow) {
            true -> window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            false -> window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }
    }
}