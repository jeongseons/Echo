package com.example.echo

import com.example.echo.auth.UserVO
import com.example.echo.group.*
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

    @POST("api/addgroup") // 모임을 생성한다.
    fun addGroup(
        @Body addgroup : NewGroupVO
    ): Call<ResponseBody>

    @POST("api/joingroupcon") // 조건을 주어 그룹 검색한다.
    fun joinGroupCon(
        @Body findGroupVO: FindGroupVO
    ): Call<List<GroupVO>>

    @GET("api/joingroupnick") // 닉네임으로 그룹 검색한다.
    fun joinGroupNick(
        @Query("user_nick") Nick : String
    ): Call<List<GroupVO>>

    @GET("api/joingrouppro/{num}/{id}") // 그룹 프로필 조회
    fun joinGroupPro(
        @Path("num") num: Int,
        @Path("id") id : String
    ): Call<JoinGroupVO>

    @POST("api/groupsignup/{num}") // 그룹 가입신청.
    fun groupSignUp(
        @Path("num") num: Int,
        @Query("id") id : String
    ): Call<ResponseBody>

    @GET("api/grouplist") //가입한 그룹의 정보들을 가져온다.
    fun getGroup(
        @Query("user_id") id : String
    ): Call<List<GroupVO>>

    @GET("api/personlist") //가입한 그룹의 회원들을 가져온다.
    fun getPerson(
        @Query("group_seq") seq : Int
    ): Call<List<PersonVO>>

    @GET("api/dropuser") // 그룹원 강퇴.
    fun dropUser(
        @Query("user_nick") nick : String
    ): Call<ResponseBody>

    @POST("api/addcal") // 일정을 추가한다..
    fun addCal(
        @Body addCal : NewDateVO
    ): Call<ResponseBody>

    @GET("api/callist") //한 그룹에 대해 추가되어 있는 일정들을 가져온다.
    fun getCalList(
        @Query("group_seq") seq : Int
    ): Call<List<NewDateVO>>

    @POST("api/groupagree/{num}") // 그룹 가입 허가
    fun groupAgree(
        @Path("num") num : Int,
        @Query("user_nick") nick : String
    ): Call<ResponseBody>

    @POST("api/groupdegree/{num}") // 그룹 가입 거절, 탈퇴
    fun groupDegree(
        @Path("num") num : Int,
        @Query("user_nick") nick : String
    ): Call<ResponseBody>

    @GET("api/getsignuplist/{num}") //그룹 가입대기 회원들을 가져온다.
    fun getSignUpList(
        @Path("num") num : Int
    ): Call<List<PersonVO>>

}

