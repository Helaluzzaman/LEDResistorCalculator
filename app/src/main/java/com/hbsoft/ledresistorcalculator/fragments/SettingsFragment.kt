package com.hbsoft.ledresistorcalculator.fragments

import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.hbsoft.ledresistorcalculator.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onResume() {
        super.onResume()
        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun onPause() {
        super.onPause()
        preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
    private val listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
        if(sharedPreferences.getBoolean(getString(R.string.Mode_key), false)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            Log.i("sharedPreference", sharedPreferences.getBoolean("Mode", false).toString())
        }else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}