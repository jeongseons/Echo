package com.example.echo.myPage

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FBdatabase {

    //realTime database 사용은 이 클래스를 통해서 진행
    companion object {
        val database = Firebase.database
        //   FBdatabase.getBoardRef()를 통해 호출
        fun getMemberRef(): DatabaseReference{
            return database.getReference("member")
        }
        fun getContentRef() : DatabaseReference{
            return database.getReference("Content")
        }
    }
}


