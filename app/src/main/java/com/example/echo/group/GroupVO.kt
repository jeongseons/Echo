package com.example.echo.group

//가입한 그룹리스트 띄우기용 모델
data class GroupVO(val profile:Int, val king:Boolean,
                   val title:String, val person:String
                   ) {
}

data class NewGroupVO(val group_owner_id : String,
                      val group_profile_img:Int,
                      val group_name : String,
                      val group_area : String,
                      val user_max : String,
                      val group_age : String,
                      val group_level : String,
                      val group_dt : String,
                      val group_yn : Boolean,
                      val group_gender : String,
                      val group_type : String,
                      val group_detail : String){

}


