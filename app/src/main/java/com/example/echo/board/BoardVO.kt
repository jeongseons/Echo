package com.example.echo.board

data class BoardVO(
    var board_seq: Int?,
    var board_title: String ="",
    var board_content: String = "",
    var board_file: String = "",
    var board_dt: String? = "",
    var user_id: String = "",
    var mnt_name: String = "",
                          ) {
}

data class BoardListVO(
    var board_seq: Int,
    var board_title: String ="",
    var board_content: String = "",
    var board_file: String = "",
    var board_dt: String = "",
    var user_id: String = "",
    var mnt_name: String = "",
    var user_nick: String = "",
    var board_reco_cnt: Int
) {
}