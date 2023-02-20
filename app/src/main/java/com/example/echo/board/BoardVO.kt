package com.example.echo.board

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BoardVO(
    var board_seq: Int?,
    var board_title: String ="",
    var board_content: String = "",
    var board_file: String = "",
    var board_dt: String? = "",
    var user_id: String = "",
    var mnt_name: String = "",
    var course_seq: Int,
                          ) {
}

data class BoardListVO(
    @SerializedName("board_seq")
    var board_seq: Int,
    @SerializedName("board_title")
    var board_title: String ="",
    @SerializedName("board_content")
    var board_content: String = "",
    @SerializedName("board_file")
    var board_file: String = "",
    @SerializedName("board_dt")
    var board_dt: String = "",
    @SerializedName("user_id")
    var user_id: String = "",
    @SerializedName("mnt_name")
    var mnt_name: String = "",
    @SerializedName("course_seq")
    var course_seq: Int,
    @SerializedName("user_nick")
    var user_nick: String = "",
    @SerializedName("user_profile_img")
    var user_profile_img: String ="",
    @SerializedName("board_reco_cnt")
    var board_reco_cnt: Int,
    @SerializedName("board_cmt_cnt")
    var board_cmt_cnt: Int,
    @SerializedName("course_img")
    var course_img: String ="",
): Serializable {
}