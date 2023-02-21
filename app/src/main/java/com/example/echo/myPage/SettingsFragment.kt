package com.example.echo.myPage

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.example.echo.R

class SettingsFragment : PreferenceFragmentCompat() {


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
//     val etPreference:EditTextPreference? = findPreference("etPreference")
//        etPreference?.isVisible = true
//        addPreferencesFromResource(R.xml.setting)
        setPreferencesFromResource(R.xml.setting, rootKey)


        val pfSos =  findPreference<SwitchPreference>("pfSos")
        pfSos?.setOnPreferenceChangeListener { preference, newValue ->
            Toast.makeText(
                requireContext(), "변경된 사항은 앱을 다시 실행해야 반영됩니다",
                Toast.LENGTH_SHORT
            ).show()
            true
        }

    }



}



