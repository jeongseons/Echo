package com.example.echo.auth

import com.example.echo.myPage.MyPageVO
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface UserAPI {

    // 회원가입
    @POST("api/user")
    fun joinUser(
        @Body user : UserVO
    ): Call<ResponseBody>

    // 로그인체크
    @GET("api/login/{user_id}")
    fun loginUser(
        @Path("user_id") user_id : String
    ): Call<ResponseBody>

    // 프로필 수정
    @PUT("api/user")
    fun modifyUser(
        @Body user: UserVO
    ): Call<ResponseBody>

    // 회원탈퇴
    @DELETE("api/user/{user_id}")
    fun deleteUser(
        @Path("user_id") user_id : String
    ): Call<ResponseBody>

    // 마이페이지 정보 가져요기
    @GET("api/user/{user_id}")
    fun getMyPage(
        @Path("user_id") user_id : String
    ): Call<MyPageVO>

}