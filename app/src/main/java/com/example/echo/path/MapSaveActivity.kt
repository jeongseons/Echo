package com.example.echo.path


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.echo.HomeFragment
import com.example.echo.MainActivity
import com.example.echo.R
import com.example.echo.myPage.MyPageFragment


class MapSaveActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_save)



        val btnSaveCoursesave = findViewById<Button>(R.id.btnSaveCoursesave)
        val btnSaveBack = findViewById<ImageView>(R.id.btnSaveBack)
        val tvMapSaveAlt = findViewById<TextView>(R.id.tvMapSaveAlt)
        val tvMapSaveDistance = findViewById<TextView>(R.id.tvMapSaveDistance)
        val tvMapSaveDurationofTime = findViewById<TextView>(R.id.tvMapSaveDurationofTime)
        val etMapSaveTitle = findViewById<EditText>(R.id.etMapSaveTitle)


        btnSaveBack.setOnClickListener {

            finish()

        }

        btnSaveCoursesave.setOnClickListener {
        val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("tvMapSaveAlt", tvMapSaveAlt.toString())
            intent.putExtra("tvMapSaveDistance", tvMapSaveDistance.toString())
            intent.putExtra("tvMapSaveDurationofTime", tvMapSaveDurationofTime.toString())
            intent.putExtra("etMapSaveTitle", etMapSaveTitle.toString())
            startActivity(intent)

        }

    }
}