package com.example.echo.group.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.echo.R
import com.example.echo.databinding.ActivityDetailPersonProfileBinding

class DetailPersonProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPersonProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_detail_person_profile)

        binding = ActivityDetailPersonProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user_nick = intent.getStringExtra("user_nick")

        binding.tvDetailPersonProfileNick.text = user_nick

    }
}