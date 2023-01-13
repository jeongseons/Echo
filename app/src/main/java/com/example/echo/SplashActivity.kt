package com.example.echo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.echo.auth.IntroActivity
import com.example.echo.auth.JoinActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // postDelayed ({실행할 코드},지연시간)
        Handler().postDelayed({//
            val intent = Intent(this@SplashActivity, JoinActivity::class.java)
            startActivity(intent)
        },2000)

    }
}