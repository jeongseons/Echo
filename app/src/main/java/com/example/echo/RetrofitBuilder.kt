package com.example.echo

import com.example.echo.auth.userApi
import com.example.echo.board.BoardApi
import com.google.gson.GsonBuilder
import com.kakao.sdk.user.UserApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    var api: API
    var userAPI : userApi
    var boardApi : BoardApi
    var gson = GsonBuilder().setLenient().create()
    init {
        var retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8099/echo/")
//            .baseUrl("https://9ea6-121-147-0-214.jp.ngrok.io/echo/")
//            .baseUrl("http://localhost:8090/echo/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        api = retrofit.create(API::class.java)
        userAPI = retrofit.create(userApi::class.java)
        boardApi = retrofit.create(BoardApi::class.java)
    }


}