package com.example.echo.auth

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import com.example.echo.R
import com.example.echo.RetrofitBuilder
import com.kakao.sdk.user.UserApiClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JoinActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        var user_id = ""
        var etUserNick = findViewById<EditText>(R.id.etUserNick)
        var etUserBirthdate =  findViewById<EditText>(R.id.etUserBirthdate)
        var imgUserProfile = findViewById<ImageView>(R.id.imgUserProfile)
        var rdoJoinFemale = findViewById<RadioButton>(R.id.rdoJoinFemale)
        var rdoJoinMale = findViewById<RadioButton>(R.id.rdoJoinMale)

        var btnUserJoin = findViewById<Button>(R.id.btnUserJoin)

        UserApiClient.instance.me { user, error ->
            user_id = user?.id.toString()
        }


        btnUserJoin.setOnClickListener {

            var user_nick = etUserNick.text.toString()
            var user_birthdate = etUserBirthdate.text.toString()
            var user_profile_img = ""
            var user_gender = ""

            var user = UserVO(user_id, user_nick, user_birthdate, user_profile_img, user_gender)

        }

    }

    fun userJoin(user : UserVO) {
        val call = RetrofitBuilder.api.userJoin(user)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>
            ) {
                Log.d("외않되", "가입성공")
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("외않되", t.localizedMessage)
            }
        })
}
}