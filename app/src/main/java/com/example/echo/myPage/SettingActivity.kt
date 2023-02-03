package com.example.echo.myPage

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat


import com.example.echo.R


class SettingActivity : AppCompatActivity(),


    PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)


    }

//    private var counter = 0
//    override fun onResume() {
//        super.onResume()
//        counter = SharedPreferences.getString("COUNTER", counter)
//        textView.text = "Counter:$counter"
//    }

    override fun onPreferenceStartFragment(
        caller: PreferenceFragmentCompat,
        pref: Preference,
    ): Boolean {
        // 새로운 프래그먼트 인스턴스화
        val args = pref.extras
        val fragment = supportFragmentManager.fragmentFactory.instantiate(
            classLoader,
            pref.fragment!!
        )
        fragment.arguments = args
        supportFragmentManager.beginTransaction().replace(R.id.fragment,
            SettingsFragment()).addToBackStack(null).commit()
        return true



        val sharedPref = getSharedPreferences("myPref", MODE_PRIVATE)

        val editor = sharedPref.edit()

        editor.putString("pfPush","value")
        editor.putString("pfLogin","value")
        editor.commit()


//    class SettingFragment : PreferenceFragmentCompat() {
//        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
//            addPreferencesFromResource(R.xml.setting)
//        }
//    }

    }


}

