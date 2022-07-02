package com.kerencev.mynasa.view.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kerencev.mynasa.R
import com.kerencev.mynasa.model.helpers.SPreference

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(SPreference.getCurrentTheme(this))
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MainFragment())
                .commitAllowingStateLoss()
        }
    }
}