package com.example.echo.group

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.system.Os.close
import android.util.Log
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.echo.HomeFragment
import com.example.echo.R
import com.example.echo.WebSocketListener
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
import io.reactivex.internal.subscriptions.SubscriptionHelper.cancel
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import org.json.JSONObject
import java.net.URISyntaxException
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

class GroupActivity : AppCompatActivity() {

    companion object{

        private var client = OkHttpClient()
        val listener: WebSocketListener = WebSocketListener()
        var grNum = 0
        lateinit var request: Request
        lateinit var testSocket: WebSocket

        fun closeSocket(){
            listener.onClose(testSocket)
//        testSocket.send("test message")
//        client.dispatcher().executorService().shutdown()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)

        val tvGroupTitle2 = findViewById<TextView>(R.id.tvGroupTitle2)
        val flGroup = findViewById<FrameLayout>(R.id.flGroup)
        val bnvGroup = findViewById<BottomNavigationView>(R.id.bnvGroup)
        val imgGroupMoveBack = findViewById<ImageView>(R.id.imgGroupMoveBack)
        imgGroupMoveBack.setOnClickListener {
            finish()
        }

        val intent = getIntent()

        //선택한 그룹명
        val title = intent.getStringExtra("title")
        tvGroupTitle2.setText(title)

        //그룹번호 정보(소켓서버 오픈용)
        grNum = intent.getIntExtra("num", 0)

        //그룹에 속한 사람이 아닐 경우 모임 정보 페이지
        if(grNum != null) {
            Log.d("test", grNum.toString())
            request = Request.Builder()
                .url("ws://smartin.kbizit.kr:8234/echo/chatting/${grNum}")
                .build()
            testSocket = client.newWebSocket(request, listener)
        }

//        if (grNum != null) {
//            runChatSocket()
//        }



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

    fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(
            R.id.flGroup,
            fragment
        ).commit()
    }
}