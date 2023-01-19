package com.example.echo.auth

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

interface userApi {
    //로그인체크
    @POST("api/login")
    fun userLogin(
        @Body user_id: String
    ): Call<ResponseBody>

    // 회원탈퇴
    @DELETE("api/user/{user_id}")
    fun deleteUser(
        @Path("user_id") user_id : String
    ): Call<ResponseBody>

}