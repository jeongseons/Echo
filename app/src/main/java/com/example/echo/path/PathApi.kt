package com.example.echo.path

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface PathApi {

    //경로저장
    @POST("api/getCourse")
    fun getCourse(
        @Body Map : MapVO
    ): Call<ResponseBody>





}