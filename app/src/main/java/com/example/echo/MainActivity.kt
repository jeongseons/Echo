package com.example.echo

import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.echo.board.BoardFragment
import com.example.echo.group.GroupFragment
import com.example.echo.myPage.MyPageFragment
import com.example.echo.path.MapFragment2
import com.example.echo.path.RecordMapFragment
//import com.example.echo.path.MapFragment2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kakao.sdk.common.util.Utility


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
                             RecordMapFragment()
                         ).commit()
        }

        bnvMain.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.tab1 -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.flMain,
                        RecordMapFragment()
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
