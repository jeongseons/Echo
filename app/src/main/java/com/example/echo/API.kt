package com.example.echo

import com.example.echo.auth.UserVO
import com.example.echo.group.GroupVO
import com.example.echo.group.NewGroupVO
import com.example.echo.group.detail.PersonVO
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.Body
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
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
        @Body user_id: String
    ): Call<ResponseBody>

    @POST("api/addgroup") //
    fun addGroup(
        @Body addgroup : NewGroupVO
    ): Call<ResponseBody>

    @GET("api/grouplist") //가입한 그룹의 정보들을 가져온다.
    fun getGroup(
        @Query("user_id") id : String
    ): Call<List<GroupVO>>

    @GET("api/personlist") //가입한 그룹의 회원들을 가져온다.
    fun getPerson(
        @Query("group_seq") seq : Int
    ): Call<List<PersonVO>>

    @GET("api/dropuser")
    fun dropUser(
        @Query("user_nick") nick : String
    ): Call<ResponseBody>
}