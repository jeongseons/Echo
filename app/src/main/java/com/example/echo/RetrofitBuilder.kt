package com.example.echo

import com.example.echo.auth.UserAPI
import com.example.echo.board.BoardAPI
import com.example.echo.board.social.SocialAPI
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitBuilder {
    var api: API
    var userApi : UserAPI
    var boardApi : BoardAPI
    var socialApi : SocialAPI
    var gson = GsonBuilder().setLenient().create()
    init {
        var retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8099/echo/")
//            .baseUrl("https://9ea6-121-147-0-214.jp.ngrok.io/echo/")
//            .baseUrl("http://localhost:8090/echo/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        api = retrofit.create(API::class.java)
        userApi = retrofit.create()
        boardApi = retrofit.create()
        socialApi = retrofit.create()
    }


}