package com.example.echo

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    var api: API
    var gson = GsonBuilder().setLenient().create()
    init {
        var retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8090/echo/")
//            .baseUrl("http://localhost:8090/echo/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        api = retrofit.create(API::class.java)
    }

}