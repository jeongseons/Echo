package com.example.echo.group

import android.content.ContentValues.TAG
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
import io.reactivex.disposables.Disposable
import okhttp3.OkHttpClient

class GroupActivity : AppCompatActivity() {

    lateinit var stompConnection: Disposable
    lateinit var topic: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)

        val tvGroupTitle2 = findViewById<TextView>(R.id.tvGroupTitle2)
        val flGroup = findViewById<FrameLayout>(R.id.flGroup)
        val bnvGroup = findViewById<BottomNavigationView>(R.id.bnvGroup)

        //선택한 그룹명
        val title = intent.getStringExtra("title")
        tvGroupTitle2.setText(title)

        //그룹번호 정보(소켓서버 오픈용)
        val num = intent.getStringExtra("num")



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
    fun runstomp(){
        val url = "ws://example.com/endpoint"
        val intervalMillis = 1000L
        val client = OkHttpClient()

        val stomp = StompClient(client, intervalMillis).apply { this@apply.url = url }
// connect
        stompConnection = stomp.connect().subscribe {
            when (it.type) {
                Event.Type.OPENED -> {
                    Log.d(TAG,"test")
                }
                Event.Type.CLOSED -> {
//                     unsubscribe
                    topic.dispose()
                }
                Event.Type.ERROR -> {
                    Log.d(TAG,"test")
                }
            }
        }
        stomp.join("/destination").subscribe { it ->
            val responseData = JSONObject(it).getString("message")

            val modelList = Gson().fromJson<ArrayList<Model>>(
                responseData, TypeToken.getParameterized(
                    MutableList::class.java,
                    Model::class.java
                ).type)
        }
// subscribe
        topic = stomp.join("/destination").subscribe { Log.i(TAG, it) }



// send
        stomp.send("/destination", "dummy message").subscribe {
            if (it) {
            }
        }

// disconnect
        stompConnection.dispose()
    }

    fun changeFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(
            R.id.flGroup,
            fragment
        ).commit()
    }
}