package com.example.echo.auth

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.util.Log
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.echo.MainActivity
import com.example.echo.R
import com.example.echo.RetrofitBuilder
import com.google.firebase.ktx.Firebase
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
    var user_nick = ""
    var isJoinSuccess = ""
    lateinit var imgJoinUserProfileEdit: ImageView
    lateinit var imgJoinUserProfileProfile: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        var etJoinUserNick = findViewById<EditText>(R.id.etJoinUserNick)
        imgJoinUserProfileEdit = findViewById(R.id.imgJoinUserProfileEdit)

        imgJoinUserProfileProfile = findViewById(R.id.imgJoinUserProfileProfile)

        imgJoinUserProfileProfile.setImageResource(R.drawable.profile)
        var rdoUserGender = findViewById<RadioGroup>(R.id.rdoUserGender)

        var btnUserJoin = findViewById<Button>(R.id.btnUserJoin)

        var npYear = findViewById<NumberPicker>(R.id.npYear)
        var npMonth = findViewById<NumberPicker>(R.id.npMonth)
        var npDay = findViewById<NumberPicker>(R.id.npDay)

        var yearList = (1950..2023).toList()
        var monthList = (1..12).toList()
        var dateList = (1..31).toList()

        var yearStrConvertList = yearList.map { it.toString() }
        var monthStrConvertList = monthList.map { it.toString() }
        var dateStrConvertList = dateList.map { it.toString() }

        npYear.run {
            minValue = 1950

            maxValue = 2023
            value = 2000
            wrapSelectorWheel = true
            displayedValues = yearStrConvertList.toTypedArray()
            findInput(this)?.inputType = InputType.TYPE_CLASS_NUMBER
        }

        npMonth.run {
            minValue = 1
            maxValue = 12
            wrapSelectorWheel = true
            displayedValues = monthStrConvertList.toTypedArray()
        }

        npDay.run {
            minValue = 1
            maxValue = 31
            wrapSelectorWheel = true
            displayedValues = dateStrConvertList.toTypedArray()
        }

        UserApiClient.instance.me { user, error ->
            user_id = user?.id.toString()
        }

        // ?????? ?????? ????????? ?????????
        rdoUserGender.setOnCheckedChangeListener { _, checkedId ->
            Log.d(" ", "RadioButton is Clicked")
            when (checkedId) {
                R.id.rdoJoinFemale -> {
                    user_gender = "???"
                }
                R.id.rdoJoinMale -> {
                    user_gender = "???"
                }
            }
            Log.d("?????????", user_gender)

        }

        // ????????? ?????? ?????????
        imgJoinUserProfileEdit.setOnClickListener {

           Toast.makeText(this, "??????",Toast.LENGTH_SHORT).show()
            val intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI
            )
            launcher.launch(intent)
        }


        btnUserJoin.setOnClickListener {
            var joinCk = true //???????????? ?????? ??????
            var user_birthMonth = "${npMonth.value}"
            var user_birthDay = "${npDay.value}"
            imgUpload(user_id)
            user_nick = etJoinUserNick.text.toString()
            if(npMonth.value < 10) {
                user_birthMonth = "0${npMonth.value}"
            }
            if(npDay.value < 10) {
                user_birthDay = "0${npDay.value}"
            }

            var user_birthdate = "${npYear.value}${user_birthMonth}${user_birthDay}"
            Log.d("test",user_birthdate)
            var user_profile_img = "https://firebasestorage.googleapis.com/v0/b/echo-73cf6.appspot.com/o/${user_id}.png?alt=media"
            Log.d("?????????",user_profile_img)

            if(user_nick.isEmpty()){
                joinCk = false
                Toast.makeText(this,"???????????? ??????????????????",Toast.LENGTH_SHORT).show()
            }

            if(user_gender.isEmpty()){
                joinCk = false
                Toast.makeText(this,"????????? ??????????????????",Toast.LENGTH_SHORT).show()
            }

            if(joinCk) {
                var user = UserVO(user_id, user_nick, user_birthdate, user_profile_img, user_gender,"n")
                joinUser(user)
            }
        }

    }

    // ????????? ????????? ?????????
    fun imgUpload(key:String){
        val storage = Firebase.storage
        val storageRef = storage.reference
        val mountainsRef = storageRef.child("$user_id.png")

        // Get the data from an ImageView as bytes
        imgJoinUserProfileEdit.isDrawingCacheEnabled = true
        imgJoinUserProfileEdit.buildDrawingCache()
        val bitmap = (imgJoinUserProfileEdit.drawable as BitmapDrawable).bitmap
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

    // ????????? ????????? ?????? ????????????
    val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            imgJoinUserProfileProfile.setImageURI(it.data?.data)
        }
    }

    fun joinUser(user:UserVO){
        val call = RetrofitBuilder.userApi.joinUser(user)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>
            ) {
                isJoinSuccess = response.body()!!.string()
                Log.d("test-isJoin", isJoinSuccess)

                if(isJoinSuccess=="success") {
                    Toast.makeText(
                        this@JoinActivity, "${user_nick}??? ???????????????",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this@JoinActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }else {
                    Toast.makeText(
                        this@JoinActivity, "????????? ?????????????????? ?????? ??????????????????",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("test-????????????", t.localizedMessage)
            }
        })
    }

    fun findInput(viewGroup: ViewGroup): EditText? {
        var childCount = viewGroup.childCount
        for (i in 0 until childCount) {
            var childView = viewGroup.getChildAt(i)
            if (childView is ViewGroup) {
                findInput(childView)
            } else if (childView is EditText) {
                return childView
            }
        }
        return null
    }


}