package com.example.echo.myPage

import android.content.ContentValues
import android.content.Entity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Button
import com.bumptech.glide.Glide
import com.example.echo.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.kakao.sdk.user.UserApiClient
import de.hdodenhof.circleimageview.CircleImageView

class ReviseActivity : AppCompatActivity() {

    lateinit var imgPhoto: ImageView
    var user_id = ""
    var storage: FirebaseStorage = Firebase.storage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_revise)

        imgPhoto = findViewById(R.id.imgPhoto)
        val tvPro = findViewById<TextView>(R.id.tvPro)
        val etEdNick = findViewById<EditText>(R.id.etEdNick)
        val btnCheck = findViewById<Button>(R.id.btnCheck)
        val btnCancel = findViewById<Button>(R.id.btnCancel)

        btnCancel.setOnClickListener {
            finish()
        }
        // 1. db에 있는 사진 불러와서
        // 프로필 사진을 설정(set)

        tvPro.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.setType("Profile image/")

        }
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(ContentValues.TAG, "사용자 정보 요청 실패", error)
            } else if (user != null) {
                Log.i(ContentValues.TAG, "사용자 정보 요청 성공" +
                        "\n회원번호: ${user.id}" +
                        "\n이메일: ${user.kakaoAccount?.email}" +
                        "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                        "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}"
                        +
                        "\n연령대: ${user.kakaoAccount?.ageRange}"
                        +
                        "\n성별: ${user.kakaoAccount?.gender}")

                user_id = user.id.toString()
                Log.d("test", "$user_id")
                getImageData(user_id)

            }
        }
    }

    fun getImageData(key: String) {
        val storageReference = Firebase.storage.reference.child("$key.png")

        storageReference.downloadUrl.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                //Gilde: 웹에 있는 이미지 적용하는 라이브러리
                Glide.with(this)
                    .load(task.result)
                    .into(imgPhoto) //지역변수

            }

        }

    }
}


// 1) fireStorage
// 접근, 이미지를 받아야와야 함
// 그 이미지는 파일명이 {id}.png
//  (1) id없이 이미지를 불러올 수 있다!!
//  (2) id가 있어야 이미지를 불러올 수 있다!!
//   마이페이지를 도착할 때! 나는 id값을 가지고 있어야 함


// 2. 프로필 사진 등록 버튼 누르면
// 내 갤러리에 있는 사진 한 장 불러와서
// 임시로 가지고 있자!!

// 3. db에 있는 사진 불러올 때, 닉네임도 같이 불러와주세요

// 4. 수정할 닉네임이 있다!!
// 닉네임을 사용자가 작성

// 5.저장!!
//  1) 프로필 사진 변경!! => 사진을 db로 전송!! (update)
//  2) 닉네임 변경!! => 닉네임도 db로 전송!! (update)



















