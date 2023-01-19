package com.example.echo

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.echo.auth.IntroActivity
import com.example.echo.board.BoardFragment
import com.example.echo.board.BoardPostFragment
import com.example.echo.group.GroupFragment
import com.example.echo.myPage.MyPageFragment
import com.example.echo.path.MapFragment
import com.example.echo.path.MapFragment2
//import com.example.echo.path.MapFragment2
import com.example.echo.path.PathFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_map_save.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Tag


class MainActivity : AppCompatActivity() {
    lateinit var tvHello: TextView
    lateinit var imgMainUserProfile:ImageView
    var user_id = ""
    var moveCk = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvHello = findViewById<TextView>(R.id.tvHello)
        hel() //스프링 데이터 확인

        val flMain = findViewById<FrameLayout>(R.id.flMain)
        val bnvMain = findViewById<BottomNavigationView>(R.id.bnvMain)
        val tvLogout = findViewById<TextView>(R.id.tvLogout)

        //하단 메뉴바 아이콘 색상 틴트 빼기
        bnvMain.setItemIconTintList(null);
        imgMainUserProfile = findViewById<CircleImageView>(R.id.imgMainUserProfile)

        var keyHash = Utility.getKeyHash(this)
        Log.d("key", keyHash)


        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "사용자 정보 요청 실패", error)
            }
            else if (user != null) {
                Log.i(TAG, "사용자 정보 요청 성공" +
                        "\n회원번호: ${user.id}" +
                        "\n이메일: ${user.kakaoAccount?.email}" +
                        "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                        "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}"
                +
                "\n연령대: ${user.kakaoAccount?.ageRange}"
                +
                    "\n성별: ${user.kakaoAccount?.gender}")

                user_id = user.id.toString()
                Log.d("test","$user_id")
                getImageData(user_id)

            }
        }

        tvLogout.setOnClickListener {
            kakaoLogout()
            val intent = Intent(this, IntroActivity::class.java)
            startActivity(intent)
        }


        supportFragmentManager.beginTransaction().replace(
            R.id.flMain,
            HomeFragment()
        ).commit()

        moveCk = intent.getStringExtra("tvMapSaveAlt").toString()
        if(moveCk.isNotEmpty()) {
                         supportFragmentManager.beginTransaction().replace(
                             R.id.flMain,
                             MyPageFragment()
                         ).commit()
        }

        bnvMain.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.tab1 -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.flMain,
                        MapFragment2()
                    ).commit()
                }


                R.id.tab2 -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.flMain,
                        GroupFragment()
                    ).commit()
                }

                R.id.tab3 -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.flMain,
                        BoardFragment()
                    ).commit()
                }

                R.id.tab4 -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.flMain,
                        MyPageFragment()
                    ).commit()
                }

            }

            true


        }
    }

        fun hel() {
            val call = RetrofitBuilder.api.getHello()
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        tvHello.text = response.body()?.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    tvHello.text = "응답실패"
                    Log.d("외않되", t.localizedMessage)
                }

//            override fun onResponse(call: Call<String>, response: Response<String>) {
//                if (response.isSuccessful){
//                    tvHello.text = response.body().toString()
//                }
//            }
//
//            override fun onFailure(call: Call<String>, t: Throwable) {
//                tvHello.text = "응답실패"
//                Log.d("외않되", t.localizedMessage)
//            }

            })
        }

    fun kakaoLogout() {
        // 로그아웃
        UserApiClient.instance.unlink { error ->
            if (error != null) {
                Log.e("Hello", "로그아웃 실패. SDK에서 토큰 삭제됨", error)
            } else {
                Log.i("Hello", "로그아웃 성공. SDK에서 토큰 삭제됨")
                Toast.makeText(this, "로그아웃 성공",
                    Toast.LENGTH_SHORT).show()
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
                    .into(imgMainUserProfile) //지역변수

            }
        }
    }

    fun changeFragment(index: Int){
        when(index){
            1 -> {
                supportFragmentManager.beginTransaction().replace(
                    R.id.flMain,
                    BoardPostFragment()
                ).commit()
            }
        }
    }

}
