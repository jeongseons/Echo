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
    lateinit var imgJoinUserProfile: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        var etJoinUserNick = findViewById<EditText>(R.id.etJoinUserNick)
        var etJoinUserBirth =  findViewById<EditText>(R.id.etJoinUserBirth)
        imgJoinUserProfile = findViewById<ImageView>(R.id.imgJoinUserProfile)
        var rdoUserGender = findViewById<RadioGroup>(R.id.rdoUserGender)

        var btnUserJoin = findViewById<Button>(R.id.btnUserJoin)


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
            var user_profile_img = Firebase.storage.reference.child("$user_id.png").downloadUrl.toString()
            Log.d("프로필",user_profile_img)

            var user = UserVO(user_id, user_nick, user_birthdate, user_profile_img, user_gender)

            userJoin(user)
            imgUpload(user_id)

            Toast.makeText(this, "${user_nick}님 환영합니다",
                Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

    fun userJoin(user : UserVO) {
        val call = RetrofitBuilder.api.userJoin(user)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>
            ) {
                Log.d("test-가입성공", response.body()!!.string())
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("test-가입실패", t.localizedMessage)
            }
        })
}

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