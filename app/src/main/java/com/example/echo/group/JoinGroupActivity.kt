package com.example.echo.group

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Spinner
import androidx.recyclerview.widget.RecyclerView
import com.example.echo.HomeFragment
import com.example.echo.R

class JoinGroupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_group)

        val flJoinGroupSearch = findViewById<FrameLayout>(R.id.flJoinGroupSearch)
        val btnJoinGroupSearchName = findViewById<Button>(R.id.btnJoinGroupSearchName)
        val btnJoinGroupSearchType = findViewById<Button>(R.id.btnJoinGroupSearchType)
        val imgJoinGroupMoveBack = findViewById<ImageView>(R.id.imgJoinGroupMoveBack)


        supportFragmentManager.beginTransaction().replace(
            R.id.flJoinGroupSearch,
            JoinGroupSearchTypeFragment()
        ).commit()

        btnJoinGroupSearchName.setOnClickListener { item ->
            supportFragmentManager.beginTransaction().replace(
                R.id.flJoinGroupSearch,
                JoinGroupSearchNameFragment()
            ).commit()
        }

        btnJoinGroupSearchType.setOnClickListener { item ->
            supportFragmentManager.beginTransaction().replace(
                R.id.flJoinGroupSearch,
                JoinGroupSearchTypeFragment()
            ).commit()
        }
        
        //뒤로가기 버튼 클릭 시 액티비티 종료
        imgJoinGroupMoveBack.setOnClickListener{
            finish()
        }


    }//OnCreate 바깥
}