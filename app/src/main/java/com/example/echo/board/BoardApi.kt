package com.example.echo.board

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface BoardApi {
    //모든 게시글 정보 조회
    @POST("api/board")
    fun getBoard(
    ): Call<List<PostVO>>
}