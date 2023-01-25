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
    var user_id = ""
    var moveCk = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val flMain = findViewById<FrameLayout>(R.id.flMain)
        val bnvMain = findViewById<BottomNavigationView>(R.id.bnvMain)


        //하단 메뉴바 아이콘 색상 틴트 빼기
        bnvMain.setItemIconTintList(null);


        var keyHash = Utility.getKeyHash(this)
        Log.d("key", keyHash)



//        로그아웃 주석
//        tvLoout.setOnClickListener {
//            kakaoLogout()
//            val intent = Intent(this, IntroActivity::class.java)
//            startActivity(intent)
//        }


        supportFragmentManager.beginTransaction().replace(
            R.id.flMain,
            HomeFragment()
        ).commit()

        moveCk = intent.getStringExtra("tvMapSaveAlt").toString()
        if(moveCk.isNotEmpty()) {
                         supportFragmentManager.beginTransaction().replace(
                             R.id.flMain,
                             MapFragment2()
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


//    fun kakaoLogout() {
//        // 로그아웃
//        UserApiClient.instance.unlink { error ->
//            if (error != null) {
//                Log.e("Hello", "로그아웃 실패. SDK에서 토큰 삭제됨", error)
//            } else {
//                Log.i("Hello", "로그아웃 성공. SDK에서 토큰 삭제됨")
//                Toast.makeText(this, "로그아웃 성공",
//                    Toast.LENGTH_SHORT).show()
//            }
//        }
//    }


//    fun changeFragment(index: Int){
//        when(index){
//            1 -> {
//                supportFragmentManager.beginTransaction().replace(
//                    R.id.flMain,
//                    BoardPostFragment()
//                ).commit()
//            }
//        }
//    }

}
