package com.example.echo.path

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface CourseAPI {

    // 경로 저장
    @POST("api/course")
    fun addCourse(
        @Body course:CourseVO
    ): Call<ResponseBody>

    // 경로 목록 조회
    @GET("api/course/user/{user_id}")
    fun getCourse(
        @Path("user_id") user_id: String
    ): Call<List<CourseList>>

    // 경로 지도 조회
    @GET("api/course/{course_seq}/map")
    fun getMap(
        @Path("course_seq") course_seq: Int
    ): Call<List<MapVO>>

    // 경로 정보 수정
    @PUT("api/course")
    fun modifyCourse(
        @Body modifiedCourse: ModifiedCourse
    ): Call<ResponseBody>

    // 경로 삭제
    @PUT("api/course/{course_seq}")
    fun deleteCourse(
        @Path("course_seq") course_seq: Int
    ): Call<ResponseBody>

    //경로 다수 삭제 - 마이페이지
    @HTTP(method = "DELETE", path="api/course",hasBody = true)
    fun deleteSelectedCourse(
        @Body courseSeqList: List<Int>
    ): Call<ResponseBody>

}