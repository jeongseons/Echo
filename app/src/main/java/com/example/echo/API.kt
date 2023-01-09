package com.example.echo

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET

interface API {
    @GET("api/hello")
    fun getHello(): Call<ResponseBody>
}