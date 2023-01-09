package com.example.echo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.TextView
import com.example.echo.board.BoardFragment
import com.example.echo.group.GroupFragment
import com.example.echo.myPage.MyPageFragment
import com.example.echo.path.PathFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var tvHello: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvHello = findViewById<TextView>(R.id.tvHello)
        hel()

        val flMain = findViewById<FrameLayout>(R.id.flMain)
        val bnvMain = findViewById<BottomNavigationView>(R.id.bnvMain)

        supportFragmentManager.beginTransaction().replace(
            R.id.flMain,
            HomeFragment()
        ).commit()

        bnvMain.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.tab1 -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.flMain,
                        PathFragment()
                    ).commit()
                }


                R.id.tab2 -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.flMain,
                        GroupFragment()
                    ).commit()
                }

                R.id.tab3 -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.flMain,
                        BoardFragment()
                    ).commit()
                }

                R.id.tab4 -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.flMain,
                        MyPageFragment()
                    ).commit()
                }

            }

            true


        }
    }

        fun hel() {
            val call = RetrofitBuilder.api.getHello()
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        tvHello.text = response.body()?.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    tvHello.text = "응답실패"
                    Log.d("외않되", t.localizedMessage)
                }

//            override fun onResponse(call: Call<String>, response: Response<String>) {
//                if (response.isSuccessful){
//                    tvHello.text = response.body().toString()
//                }
//            }
//
//            override fun onFailure(call: Call<String>, t: Throwable) {
//                tvHello.text = "응답실패"
//                Log.d("외않되", t.localizedMessage)
//            }

            })
        }
    }
