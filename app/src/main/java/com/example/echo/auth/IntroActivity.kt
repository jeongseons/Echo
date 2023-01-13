package com.example.echo.auth

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.echo.MainActivity
import com.example.echo.R
import com.example.echo.RetrofitBuilder
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class IntroActivity : AppCompatActivity() {
    var user_id = ""
    var joinCk = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        KakaoSdk.init(this, "f5b248e1f5c7496e71b711e2650daf28")

        var imgKakaoLogin = findViewById<ImageView>(R.id.imgKakaoLogin)

        var keyHash = Utility.getKeyHash(this)
        Log.d("key", keyHash)

        if (AuthApiClient.instance.hasToken()) {
            UserApiClient.instance.accessTokenInfo { _, error ->
                if (error != null) {
                    if (error is KakaoSdkError && error.isInvalidTokenError() == true) {
                        //로그인 필요
                    }
                    else {
                        //기타 에러
                    }
                }
                else {
                    //토큰 유효성 체크 성공(필요 시 토큰 갱신됨)
                    Toast.makeText(
                        this, "로그인 성공",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)

                }
            }
        }
        else {
            //로그인 필요
        }

        imgKakaoLogin.setOnClickListener {
        // 카카오톡 설치 확인
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                // 카카오톡 로그인
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    // 로그인 실패 부분
                    if (error != null) {
                        Log.e(TAG, "로그인 실패 $error")
                        // 사용자가 취소
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled ) {
                            return@loginWithKakaoTalk
                        }
                        // 다른 오류
                        else {
                            UserApiClient.instance.loginWithKakaoAccount(this, callback = mCallback) // 카카오 이메일 로그인
                        }
                    }
                    // 로그인 성공 부분
                    else if (token != null) {
                        Log.e(TAG, "로그인 성공 ${token.accessToken}")

                        UserApiClient.instance.me { user, error ->
                            user_id = user?.id.toString()
                        }

                        userLogin(user_id)

                        if (joinCk) {
                            Toast.makeText(
                                this, "로그인 성공",
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        }else{
                            val intent = Intent(this, JoinActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = mCallback) // 카카오 이메일 로그인
            }


        }

    }

    // 이메일 로그인 콜백
    val mCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e(TAG, "로그인 실패 $error")
        } else if (token != null) {
            Log.e(TAG, "로그인 성공 ${token.accessToken}")

            UserApiClient.instance.me { user, error ->
                user_id = user?.id.toString()
            }

            userLogin(user_id)

            if (joinCk) {
                Toast.makeText(
                    this, "로그인 성공",
                    Toast.LENGTH_SHORT
                ).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }else{
                val intent = Intent(this, JoinActivity::class.java)
                startActivity(intent)
            }
        }
    }

    // 기 회원가입 체크
    fun userLogin(user_id : String) {
        val call = RetrofitBuilder.api.userLogin(user_id)
        val call2 = RetrofitBuilder.userAPI.userLogin(user_id)
        call2.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.d("스프링login리스폰스", response.body().toString())
                var body = response.body()?.string()
                if(body!!.length >2){
                    joinCk = true
                    Log.d("스프링login리스폰스", "${response.body()}")
                    Log.d("스프링login리스폰스", "$body")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("외않되", t.localizedMessage)

            }


        })
    }


}


