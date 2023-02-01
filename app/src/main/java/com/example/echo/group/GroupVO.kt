package com.example.echo.group

import java.util.Date

//가입한 그룹리스트 띄우기용 모델
data class GroupVO(val group_seq:Int,
                   val group_profile_img:String,
                   val group_auth:String,
                   val group_name:String,
                   val user_max:Int,
                   val group_current:Int
                   )


//새로운 모임 생성용 모델
data class NewGroupVO(val group_owner_id : String,
                      val group_profile_img:String,
                      val group_name : String,
                      val group_area : String,
                      val user_max : Int,
                      val group_age : String,
                      val group_level : String,
                      val group_dt : String,
                      val group_yn : Boolean,
                      val group_gender : String,
                      val group_type : String,
                      val group_detail : String)



//일정 추가하기 모델
data class NewDateVO(val cal_dt : String,
                     val cal_content : String,
                     val group_seq : Int){

}

//기존 모임 찾기용 모델
data class FindGroupVO(val group_type : String,
                       val group_age : String,
                       val group_gender : String,
                       val group_level : String,
                       val group_area : String)

//가입용 모임 모델
data class JoinGroupVO(val group_seq:Int,
                       val group_profile_img:String,
                       val group_auth:String,
                       val group_current:Int,
                       val group_name : String,
                       val group_owner_id : String,
                       val group_area : String,
                       val user_max : Int,
                       val group_age : String,
                       val group_level : String,
                       val group_gender : String,
                       val group_type : String,
                       val group_detail: String)

//모임 설정 변경용 모델
data class SettingGroupVO(val group_profile_img:String,
                          val group_name : String,
                          val group_area : String,
                          val user_max : Int,
                          val group_age : String,
                          val group_level : String,
                          val group_gender : String,
                          val group_type : String,
                          val group_detail : String)

//일정 조회, 수정, 삭제용 모델
data class DateVO(val cal_seq : Int,
                  val cal_dt : String,
                     val cal_content : String,
                     val group_seq : Int){

}




