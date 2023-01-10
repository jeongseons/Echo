package com.example.echo.group

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.TextView
import com.example.echo.HomeFragment
import com.example.echo.R
import com.example.echo.board.BoardFragment
import com.example.echo.group.detail.DetailDateFragment
import com.example.echo.group.detail.DetailPersonFragment
import com.example.echo.group.detail.DetailSettingFragment
import com.example.echo.group.detail.DetailTalkFragment
import com.example.echo.myPage.MyPageFragment
import com.example.echo.path.PathFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class GroupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)

        val tvGroupTitle2 = findViewById<TextView>(R.id.tvGroupTitle2)
        val flGroup = findViewById<FrameLayout>(R.id.flGroup)
        val bnvGroup = findViewById<BottomNavigationView>(R.id.bnvGroup)

        val title = intent.getStringExtra("title")
        tvGroupTitle2.setText(title)

        supportFragmentManager.beginTransaction().replace(
            R.id.flGroup,
            DetailTalkFragment()
        ).commit()

        bnvGroup.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.grouptab1 -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.flGroup,
                        DetailTalkFragment()
                    ).commit()
                }


                R.id.grouptab2 -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.flGroup,
                        DetailPersonFragment()
                    ).commit()
                }

                R.id.grouptab3 -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.flGroup,
                        DetailDateFragment()
                    ).commit()
                }

                R.id.grouptab4 -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.flGroup,
                        DetailSettingFragment()
                    ).commit()
                }

            }
            true
        }
    }
}