package com.example.echo.board

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface BoardApi {
    @POST("api/login")
    fun userLogin(
        @Body user_id: String
    ): Call<ResponseBody>
}