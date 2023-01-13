package com.example.echo

import com.example.echo.auth.UserVO
import com.example.echo.group.GroupVO
import com.example.echo.group.detail.PersonVO
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

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

    @GET("api/grouplist") //가입한 그룹의 정보들을 가져온다.
    fun getGroup(
        @Query("user_id") id : String
    ): Call<List<GroupVO>>

    @GET("api/dropuser")
    fun dropUser(
//        @Path nick : String
    ): Call<ResponseBody>
}