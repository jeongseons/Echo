package com.example.echo.path

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MapVO(
    @SerializedName("map_seq")
    var map_seq: Int?,
    @SerializedName("course_seq")
    var course_seq: Int,
    @SerializedName("lat")
    var lat: Double,
    @SerializedName("lng")
    var lng: Double,
): Serializable {

}