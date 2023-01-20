package com.example.echo.myPage

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import com.bumptech.glide.Glide
import com.example.echo.RetrofitBuilder
import com.example.echo.auth.IntroActivity
import com.example.echo.auth.UserVO
import com.example.echo.databinding.ActivityReviseBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.kakao.sdk.user.UserApiClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class ReviseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReviseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReviseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user_id = intent.getStringExtra("user_id")
        val user_nick = intent.getStringExtra("user_nick")
        val user_profile_img = intent.getStringExtra("user_profile_img")

        binding.etReviseUserNick.setText(user_nick)
        Glide.with(this)
            .load(user_profile_img)
            .into(binding.imgReviseProfile) //지역변수

        // 프로필 사진 변경
        binding.tvReviseProfile.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI
            )
            launcher.launch(intent)
        }

        binding.btnRevise.setOnClickListener {
            imgUpload(user_id!!)
            var revisedNick = binding.etReviseUserNick.text.toString()
            modifyUser(user_id, revisedNick)
        }


    }

    // 프로필 이미지 업로드
    fun imgUpload(key:String){
        val storage = Firebase.storage
        val storageRef = storage.reference
        val mountainsRef = storageRef.child("$user_id.png")

        binding.imgReviseProfile.isDrawingCacheEnabled = true
        binding.imgReviseProfile.buildDrawingCache()
        val bitmap = (binding.imgReviseProfile.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos)
        val data = baos.toByteArray()

        var uploadTask = mountainsRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }
    }

    // 선택한 프로필 사진 보여주기
    val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            binding.imgReviseProfile.setImageURI(it.data?.data)
        }
    }

    fun modifyUser(user_id: String, revisedNick: String){
        val revisedUser = UserVO(user_id,revisedNick,"","","")
        val call = RetrofitBuilder.userApi.modifyUser(revisedUser)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    Log.d("test-수정후", response.body().toString())
                    Toast.makeText(
                        this@ReviseActivity, "정상적으로 수정되었습니다",
                        Toast.LENGTH_SHORT
                    ).show()
                }else{
                    Toast.makeText(
                        this@ReviseActivity, "다시 시도해주세요",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            }
        })
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



















