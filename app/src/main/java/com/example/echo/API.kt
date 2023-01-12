package com.example.echo

import com.example.echo.auth.UserVO
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST

interface API {
    @GET("api/hello")
    fun getHello(): Call<ResponseBody>

    @POST("api/join")
    fun userJoin(
        @Body user : UserVO
    ): Call<ResponseBody>

    @POST("api/addgroup") //
    fun postAddGroup(
        @Field("user_Nick") nick: String,
        @Field("user_Date") date: String,
        @Field("user_Id") id: String,
        @Field("user_Pic") picture: String,
        @Field("user_Gen") gender: String
    ): Call<ResponseDTO>
}