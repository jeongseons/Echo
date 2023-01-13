package com.example.echo.auth

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface userApi {
    //로그인체크
    @POST("api/login")
    fun userLogin(
        @Body user_id: String
    ): Call<ResponseBody>
}