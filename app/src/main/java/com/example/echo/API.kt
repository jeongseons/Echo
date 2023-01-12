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
    //스프링 연결 확인용
    @GET("api/hello")
    fun getHello(): Call<ResponseBody>

    //회원가입
    @POST("api/join")
    fun userJoin(
        @Body user : UserVO
    ): Call<ResponseBody>

    //로그인체크
    @POST("api/login")
    fun userLogin(
        @Field("user_id") user_id: String
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