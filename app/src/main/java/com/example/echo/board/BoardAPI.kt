package com.example.echo.board

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface BoardAPI {
    //게시글 정보 조회
    @GET("api/board")
    fun getBoard(
        @Query("id") user_id: String? = null
    ): Call<List<BoardListVO>>

    //게시글 등록
    @POST("api/board")
    fun addBoard(
        @Body board: BoardVO
    ): Call<ResponseBody>

    @DELETE("api/board/{board_seq}")
    fun deleteBoard(
        @Path("board_seq") board_seq : Int
    ): Call<ResponseBody>

    @PUT("api/board")
    fun modifyBoard(
        @Body board: BoardVO
    ): Call<ResponseBody>

}