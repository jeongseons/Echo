package com.example.echo.board.social

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface SocialAPI {
    //모든 댓글 정보 조회
    @GET("api/comment/{board_seq}")
    fun getComment(
        @Path("board_seq") board_seq : Int
    ): Call<List<CmtListVO>>

    //댓글 등록
    @POST("api/comment/{board_seq}")
    fun addComment(
        @Path("board_seq") board_seq : Int,
        @Body comment: CommentVO
    ): Call<ResponseBody>

    //댓글 삭제
    @DELETE("api/comment")
    fun deleteComment(
        @Query("seq") cmt_seq: Int,
        @Query("board_seq") board_seq: Int
    ): Call<ResponseBody>

    //추천 조회
    @GET("api/reco")
    fun getReco(
        @Query("id") user_id: String,
        @Query("board_seq") board_seq: Int
    ): Call<RecoVO>

    //추천 추가
    @POST("api/reco/{board_seq}")
    fun addReco(
        @Path("board_seq") board_seq : Int,
        @Body reco: RecoVO
    ): Call<ResponseBody>

    //추천 취소
    @DELETE("api/reco")
    fun deleteReco(
        @Query("id") user_id: String,
        @Query("board_seq") board_seq: Int
    ): Call<ResponseBody>

}