package com.example.weather.fragment

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.*
import com.example.weather.R

class SettingsFragment: PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    private fun setPreferenceSummary(preference: Preference, value: String) {
        if (preference is ListPreference) {
            val listPreference: ListPreference = preference
            val prefIndex: Int = listPreference.findIndexOfValue(value)
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.entries[prefIndex])
            }
        } else {
            preference.summary = value
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pref_general)
        val sharedPreferences = preferenceScreen.sharedPreferences
        val prefScreen: PreferenceScreen = preferenceScreen
        val count: Int = prefScreen.preferenceCount
        for (i in 0 until count) {
            val p: Preference = prefScreen.getPreference(i)
            val value = sharedPreferences.getString(p.key, "")
            if (value != null) {
                setPreferenceSummary(p, value)
            }
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        val preference: Preference = key?.let { findPreference(it) }!!
        sharedPreferences!!.getString(key, "")?.let { setPreferenceSummary(preference, it) }
    }

    override fun onStop() {
        super.onStop()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onStart() {
        super.onStart()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }
}