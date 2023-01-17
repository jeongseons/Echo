package com.example.echo.myPage



import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Profile
import android.provider.MediaStore

import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import com.example.echo.R
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class ReviseActivity : AppCompatActivity() {

    lateinit var imgPhoto: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_revise)

        imgPhoto = findViewById(R.id.imgPhoto)
        val tvPro = findViewById<TextView>(R.id.tvPro)
        val etEdNick = findViewById<EditText>(R.id.etEdNick)
        val btnCheck = findViewById<Button>(R.id.btnCheck)
        val btnCancel = findViewById<Button>(R.id.btnCancel)



        btnCancel.setOnClickListener {
            finish()
        }
        btnCheck.setOnClickListener {
            val nick = etEdNick.text.toString()
        }

    }



    }












