package com.example.echo.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.echo.MainActivity
import com.example.echo.R
import com.example.echo.RetrofitBuilder
import com.kakao.sdk.user.UserApiClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JoinActivity : AppCompatActivity() {

    var user_gender = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        var user_id = ""
        var etUserNick = findViewById<EditText>(R.id.etUserNick)
        var etUserBirthdate =  findViewById<EditText>(R.id.etUserBirthdate)
        var imgUserProfile = findViewById<ImageView>(R.id.imgUserProfile)

//        var rdoUserGender = findViewById<RadioGroup>(R.id.rdoUserGender)
//        var rdoJoinFemale = findViewById<RadioButton>(R.id.rdoJoinFemale)
//        var rdoJoinMale = findViewById<RadioButton>(R.id.rdoJoinMale)

        var btnUserJoin = findViewById<Button>(R.id.btnUserJoin)

        UserApiClient.instance.me { user, error ->
            user_id = user?.id.toString()
        }


        btnUserJoin.setOnClickListener {

            var user_nick = etUserNick.text.toString()
            var user_birthdate = etUserBirthdate.text.toString()
            var user_profile_img = "https://cdn.kormedi.com/wp-content/uploads/2019/09/512.jpg"

            var user = UserVO(user_id, user_nick, user_birthdate, user_profile_img, user_gender)

            userJoin(user)

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
                Log.d("외않되", response.body()!!.string())
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("외않되", t.localizedMessage)
            }
        })
}

    //성별 라디오버튼 클릭 이벤트 핸들러
    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.rdoJoinFemale ->
                    if (checked) {
                        user_gender = "여"
                    }
                R.id.rdoJoinMale ->
                    if (checked) {
                        user_gender = "남"
                    }
            }
        }
    }
}