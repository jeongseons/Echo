package com.example.echo.myPage

import com.example.echo.auth.UserVO
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface MyPageApi {
    // 마이페이지 정보 가져요기
    @GET("api/user/{user_id}")
    fun getMyPage(
        @Path("user_id") user_id : String
    ): Call<MyPageVO>

    // 프로필 수정
    @PUT("api/user")
    fun modifyUser(
        @Body user: UserVO
    ): Call<ResponseBody>

    // 작성한 글 조회
    @GET("api/board")
    fun getMyBoard(
        @Query("id") user_id : String
    ): Call<ResponseBody>

    // 나의 경로 조회
    @GET("api/course")
    fun getMyCourse(
        @Query("id") user_id : String
    ): Call<ResponseBody>

}