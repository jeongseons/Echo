package com.example.echo.myPage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.example.echo.R

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)


        supportFragmentManager
            .beginTransaction()
            .replace(R.id.tvMyPageSetting,SettingFragment())
            .commit()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

                }
            }

