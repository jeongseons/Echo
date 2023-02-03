package com.example.echo.myPage

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.echo.R

class SettingsFragment : PreferenceFragmentCompat() {


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
//     val etPreference:EditTextPreference? = findPreference("etPreference")
//        etPreference?.isVisible = true
//        addPreferencesFromResource(R.xml.setting)
        setPreferencesFromResource(R.xml.setting, rootKey)





    }



}



