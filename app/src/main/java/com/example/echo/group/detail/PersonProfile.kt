package com.example.echo.group.detail

import com.example.echo.board.BoardListVO
import com.example.echo.myPage.MyPageVO
import com.example.echo.path.CourseList

data class PersonProfile(
    val personInfo: MyPageVO? = null,
    val boardList: List<BoardListVO>? = null,
    val courseList: List<CourseList>? = null,
){

}