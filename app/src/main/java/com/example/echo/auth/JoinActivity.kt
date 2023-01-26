package com.example.echo.auth

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.echo.MainActivity
import com.example.echo.R
import com.example.echo.RetrofitBuilder
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.kakao.sdk.user.UserApiClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class JoinActivity : AppCompatActivity() {

    var user_gender = ""
    var user_id = ""
    var isJoinSuccess = ""
    lateinit var imgJoinUserProfile: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        var etJoinUserNick = findViewById<EditText>(R.id.etJoinUserNick)
        var etJoinUserBirth =  findViewById<EditText>(R.id.etJoinUserBirth)
        imgJoinUserProfile = findViewById<ImageView>(R.id.imgJoinUserProfile)
        imgJoinUserProfile.setImageResource(R.drawable.profile)
        var rdoUserGender = findViewById<RadioGroup>(R.id.rdoUserGender)

        var btnUserJoin = findViewById<Button>(R.id.btnUserJoin)

        var dpSpinner = findViewById<DatePicker>(R.id.dpSpinner)

        var yearList = (1950..2020).toList()
        var monthList = (1..12).toList()
        var dateList = (1..31).toList()

        var yearStrConvertList = yearList.map { it.toString() }
        var monthStrConvertList = monthList.map { it.toString() }
        var dateStrConvertList = dateList.map { it.toString() }
//
//        npYear.run {
//            minValue = 0
//            maxValue = yearStrConvertList.size - 1
//            wrapSelectorWheel = false
//            displayedValues = yearStrConvertList.toTypedArray()
//        }
//
//        npMonth.run {
//            minValue = 0
//            maxValue = monthStrConvertList.size - 1
//            wrapSelectorWheel = false
//            displayedValues = monthStrConvertList.toTypedArray()
//        }
//
//        npDay.run {
//            minValue = 0
//            maxValue = dateStrConvertList.size - 1
//            wrapSelectorWheel = false
//            displayedValues = dateStrConvertList.toTypedArray()
//        }

        UserApiClient.instance.me { user, error ->
            user_id = user?.id.toString()
        }

        // 성별 선택 이벤트 핸들러
        rdoUserGender.setOnCheckedChangeListener { _, checkedId ->
            Log.d(" ", "RadioButton is Clicked")
            when (checkedId) {
                R.id.rdoJoinFemale -> {
                    user_gender = "여"
                }
                R.id.rdoJoinMale -> {
                    user_gender = "남"
                }
            }
            Log.d("외않되", user_gender)

        }

        // 프로필 사진 이벤트
        imgJoinUserProfile.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI
            )
            launcher.launch(intent)
        }


        btnUserJoin.setOnClickListener {
            var isJoin = true //회원가입 조건 체크

            imgUpload(user_id)
            var user_nick = etJoinUserNick.text.toString()
            var user_birthdate = etJoinUserBirth.text.toString()
            var user_profile_img = "https://firebasestorage.googleapis.com/v0/b/echo-73cf6.appspot.com/o/${user_id}.png?alt=media"
            Log.d("프로필",user_profile_img)

            if(user_nick.isEmpty()){
                isJoin = false
                Toast.makeText(this,"닉네임을 입력해주세요",Toast.LENGTH_SHORT).show()

            }
            if(user_birthdate.isEmpty()){
                isJoin = false
                Toast.makeText(this,"생일을 입력해주세요",Toast.LENGTH_SHORT).show()
            }
            if(user_gender.isEmpty()){
                isJoin = false
                Toast.makeText(this,"성별을 선택해주세요",Toast.LENGTH_SHORT).show()
            }
            if(user_birthdate.length != 8){
                isJoin = false
                Toast.makeText(this,"생일을 20020101 형식으로 입력해주세요",Toast.LENGTH_SHORT).show()
            }

            if(isJoin) {
                var user = UserVO(user_id, user_nick, user_birthdate, user_profile_img, user_gender)

                    val call = RetrofitBuilder.userApi.joinUser(user)
                    call.enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>
                        ) {
                            isJoinSuccess = response.body()!!.string()
                            Log.d("test-isJoin", isJoinSuccess)

                            if(isJoinSuccess=="success") {
                                Toast.makeText(
                                    this@JoinActivity, "${user_nick}님 환영합니다",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent = Intent(this@JoinActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }else {
                                Toast.makeText(
                                    this@JoinActivity, "가입에 실패했습니다 다시 시도해주세요",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        }
                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            Log.d("test-가입실패", t.localizedMessage)
                        }
                    })

//                if(isJoinSuccess=="success") {
//                    Toast.makeText(
//                        this, "${user_nick}님 환영합니다",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    val intent = Intent(this, MainActivity::class.java)
//                    startActivity(intent)
//                    finish()
//                }else {
//                    Toast.makeText(
//                        this, "가입에 실패했습니다 다시 시도해주세요",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }

            }
        }

    }

//    fun userJoin(user : UserVO) {
//        val call = RetrofitBuilder.api.userJoin(user)
//        call.enqueue(object : Callback<ResponseBody> {
//            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>
//            ) {
//                var joinCk = response.body()!!.string()
//                if(joinCk=="success"){
//                    isJoinSuccess = true
//                }
//                Log.d("test-가입성공", joinCk)
//                Log.d("test-isJoin", isJoinSuccess.toString())
//
//            }
//            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                Log.d("test-가입실패", t.localizedMessage)
//            }
//        })
//}

    // 프로필 이미지 업로드
    fun imgUpload(key:String){
        val storage = Firebase.storage
        val storageRef = storage.reference
        val mountainsRef = storageRef.child("$user_id.png")

        // Get the data from an ImageView as bytes
        imgJoinUserProfile.isDrawingCacheEnabled = true
        imgJoinUserProfile.buildDrawingCache()
        val bitmap = (imgJoinUserProfile.drawable as BitmapDrawable).bitmap
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
            imgJoinUserProfile.setImageURI(it.data?.data)
        }
    }


}