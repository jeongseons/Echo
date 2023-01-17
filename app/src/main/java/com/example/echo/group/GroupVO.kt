package com.example.echo.group

import java.util.Date

//가입한 그룹리스트 띄우기용 모델
data class GroupVO(var group_seq:Int, var group_profile_img:String, var group_auth:String,
                   var group_name:String, var user_max:Int, var group_current:Int
                   ) {
}

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
                      val group_detail : String){

}

//일정 추가하기 모델
data class NewDateVO(val cal_dt : String,
                     val cal_content : String,
                     val group_seq : Int)





