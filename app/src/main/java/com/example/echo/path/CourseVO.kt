package com.example.echo.path

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CourseVO(
    var course_seq: Int?,
    var user_id: String = "",
    var course_title: String = "",
    var course_time: String = "",
    var course_alt: String = "",
    var course_distance: String = "",
    var course_start_dt: String = "",
    var course_end_dt: String = "",
    var course_open: String = "",
    var course_img: String = "",
    var latlngArray: ArrayList<Pair<Double,Double>>
){
}

data class CourseInfo (
    var course_seq: Int,
    var user_id: String = "",
    var course_title: String = "",
    var course_time: String = "",
    var course_alt: String = "",
    var course_distance: String = "",
    var course_start_dt: String = "",
    var course_end_dt: String = "",
    var course_open: String = "",
    var course_img: String = "",
){
}

data class CourseList (
    @SerializedName("course_seq")
    var course_seq: Int,
    @SerializedName("user_id")
    var user_id: String = "",
    @SerializedName("course_title")
    var course_title: String = "",
    @SerializedName("course_time")
    var course_time: String = "",
    @SerializedName("course_alt")
    var course_alt: String = "",
    @SerializedName("course_distance")
    var course_distance: String = "",
    @SerializedName("course_start_dt")
    var course_start_dt: String = "",
    @SerializedName("course_end_dt")
    var course_end_dt: String = "",
    @SerializedName("course_open")
    var course_open: String = "",
    @SerializedName("course_img")
    var course_img: String = "",
    @SerializedName("start_lat")
    var start_lat: Double = 0.0,
    @SerializedName("start_lng")
    var start_lng: Double = 0.0,
): Serializable {
}

data class ModifiedCourse (var course_seq:Int,
                           var course_title: String = "",
                           var course_open: String = ""
){
}