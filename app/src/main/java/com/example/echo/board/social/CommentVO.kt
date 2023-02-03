package com.example.echo.board.social

data class CommentVO(var cmt_seq: Int?,
                     var board_seq: Int,
                     var cmt_content: String,
                     var cmt_dt: String?,
                     var user_id: String
                     ){
}

data class CmtListVO(
    var cmt_seq: Int,
    var board_seq: Int,
    var cmt_content: String,
    var cmt_dt: String,
    var user_id: String,
    var user_nick: String,
    var user_profile_img: String
) {
}