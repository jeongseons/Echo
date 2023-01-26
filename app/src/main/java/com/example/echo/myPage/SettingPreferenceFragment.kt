package com.example.echo.myPage

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.example.echo.R


// PreferenceFragmentCompat을 상속받는 클래스 정의
class SettingPreferenceFragment: PreferenceFragmentCompat() {
    lateinit var prefs: SharedPreferences

    var keywordPreference: Preference? = null
    var keywordListPreference: Preference? = null

    // onCreate() 중에 호출되어 Fragment에 preference를 제공하는 메서드
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        // preference xml을 inflate하는 메서드
        setPreferencesFromResource(R.xml.setting, rootKey)

        // rootkey가 null이라면
        if (rootKey == null) {
            // Preference 객체들 초기화
            keywordPreference = findPreference("keyword")
            keywordListPreference = findPreference("keyword_sound_list")
        }

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val name = sharedPreferences.getString("signature", "")

        val prefListener =
            SharedPreferences.OnSharedPreferenceChangeListener {
                    sharedPreferences: SharedPreferences?, key: String? ->
                // key는 xml에 등록된 key에 해당
                when (key) {
                    "keyword_sound_list" -> {
                        // SharedPreferences에 저장된 값을 가져와서 summary 설정
                        val summary = prefs.getString("keyword_sound_list", "카톡")
                        keywordListPreference?.summary = summary
                    }
                    "keyword" -> {
                        val value = prefs.getBoolean("keyword", false)
                    }
                }
            }

    }
}