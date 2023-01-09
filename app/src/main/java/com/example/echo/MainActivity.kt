package com.example.echo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var tvHello : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvHello = findViewById<TextView>(R.id.tvHello)
        hel()

    }

    fun hel(){
        val call = RetrofitBuilder.api.getHello()
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful){
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