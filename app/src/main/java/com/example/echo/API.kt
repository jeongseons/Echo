package com.example.echo

import com.example.echo.auth.UserVO
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface API {
    @GET("api/hello")
    fun getHello(): Call<ResponseBody>

    @POST("api/join")
    fun userJoin(
        @Body user : UserVO
    ): Call<ResponseBody>

}