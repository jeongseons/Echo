package com.example.echo.path

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

data class CourseInfo (var course_seq: Int,
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

data class CourseList (var course_seq: Int,
                       var user_id: String = "",
                       var course_title: String = "",
                       var course_time: String = "",
                       var course_alt: String = "",
                       var course_distance: String = "",
                       var course_start_dt: String = "",
                       var course_end_dt: String = "",
                       var course_open: String = "",
                       var course_img: String = "",
                       var start_lat: Double = 0.0,
                       var start_lng: Double = 0.0,
){
}

data class ModifiedCourse (var course_seq:Int,
                           var course_title: String = "",
                           var course_open: String = ""
){
}