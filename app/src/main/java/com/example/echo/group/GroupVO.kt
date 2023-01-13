package com.example.echo.group

//가입한 그룹리스트 띄우기용 모델
data class GroupVO(var group_seq:Int, var group_profile_img:String, var group_auth:String,
                   var group_name:String, var user_max:Int, var group_current:Int
                   ) {
}