package com.kerencev.mynasa.model.helpers

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.kerencev.mynasa.R

object SPreference {

    private val KEY_SP = "sp"
    private val KEY_CURRENT_THEME = "current_theme"

    fun saveTheme(context: Context, theme: Int) {
        val pref = context.getSharedPreferences(KEY_SP, MODE_PRIVATE)
        val editor = pref.edit()
        editor.putInt(KEY_CURRENT_THEME, theme)
        editor.apply()
    }

    fun getCurrentTheme(context: Context): Int {
        val pref = context.getSharedPreferences(KEY_SP, MODE_PRIVATE)
        return pref.getInt(KEY_CURRENT_THEME, R.style.Theme_MyNasa)
    }
}