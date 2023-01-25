package com.example.echo.group

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.echo.HomeFragment
import com.example.echo.R
import com.example.echo.board.BoardFragment
import com.example.echo.group.detail.*
import com.example.echo.myPage.MyPageFragment
import com.example.echo.path.PathFragment
import com.gmail.bishoybasily.stomp.lib.Event
import com.gmail.bishoybasily.stomp.lib.StompClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.disposables.Disposable
import okhttp3.OkHttpClient
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class GroupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)

        val tvGroupTitle2 = findViewById<TextView>(R.id.tvGroupTitle2)
        val flGroup = findViewById<FrameLayout>(R.id.flGroup)
        val bnvGroup = findViewById<BottomNavigationView>(R.id.bnvGroup)

        val intent = getIntent()

        //선택한 그룹명
        val title = intent.getStringExtra("title")
        tvGroupTitle2.setText(title)

        //그룹번호 정보(소켓서버 오픈용)
        val num = intent.getIntExtra("num", 0)

        //그룹에 속한 사람이 아닐 경우 모임 정보 페이지


        if (num != null) {
            runstomp(num)
        }



        supportFragmentManager.beginTransaction().replace(
            R.id.flGroup,
            DetailTalkFragment()
        ).commit()

        bnvGroup.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.grouptab1 -> {
                    changeFragment(DetailTalkFragment())
                }

                R.id.grouptab2 -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.flGroup,
                        DetailPersonFragment()
                    ).commit()
                }

                R.id.grouptab3 -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.flGroup,
                        DetailDateFragment()
                    ).commit()
                }

                R.id.grouptab4 -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.flGroup,
                        DetailSettingFragment()
                    ).commit()
                }

            }
            true
        }

    }

    companion object{
        //전역 객체 설정.
        lateinit var stompConnection: Disposable
        lateinit var topic: Disposable

        val stompurl = "ws://172.30.1.87:8099/echo/ws/websocket"
        val intervalMillis = 1000L
        val stompclient = OkHttpClient
            .Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .build()

        val stomp = StompClient(stompclient, intervalMillis).apply { this@apply.url = stompurl }

        // send
//        stomp.send("/app/{$num}", "dummy message").subscribe {
//            if (it) {
//            }
//        }
//        Log.d("소켓", "열림")
//

        // disconnect
//        stompConnection.dispose()

        fun runstomp(num: Int) {
            // connect
            stompConnection = stomp.connect().subscribe {

                when (it.type) {
                    Event.Type.OPENED -> {
                        Log.d("소켓 it", it.toString())
//                    // subscribe
//                    topic = stomp.join("/destination").subscribe { Log.i(TAG, it) }
                        topic = stomp.join("/topic/{$num}").subscribe { it ->
                            val responseData = JSONObject(it).getString("message")

                            val modelList = Gson().fromJson<ArrayList<Message>>(
                                responseData, TypeToken.getParameterized(
                                    MutableList::class.java,
                                    Message::class.java
                                ).type
                            )
                            Log.d("메시지",modelList.toString())
                        }
                    }
                    Event.Type.CLOSED -> {
                        Log.d("소켓", "닫음")
//                     unsubscribe
//                        topic.dispose()
                    }
                    Event.Type.ERROR -> {

                    }
                    else -> {}
                }
            }
        }
    }

    fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(
            R.id.flGroup,
            fragment
        ).commit()
    }
}