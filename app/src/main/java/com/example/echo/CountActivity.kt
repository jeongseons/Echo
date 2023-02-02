package com.example.echo

import android.content.Intent
import android.media.SoundPool
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*


class CountActivity : AppCompatActivity() {
    lateinit var tvCountTimer: TextView
    private lateinit var mSoundPool: SoundPool
    private var mSoundId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_count)

        tvCountTimer = findViewById<TextView>(R.id.tvCountTimer)
        val btnCountNegative = findViewById<Button>(R.id.btnCountNegative)
        val thread = TimerThread(tvCountTimer)

        setOrderAlarm()


        thread.start()

        btnCountNegative.setOnClickListener {
            mSoundPool.release()
            finish()
        }

    }

    fun setOrderAlarm() {
        mSoundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .build()
        mSoundId = mSoundPool.load(applicationContext, R.raw.siren, 1)
    }

    fun playOrderAlarm() {
        mSoundPool.play(mSoundId!!, 10f, 10f, 0, -1, 1f)
    }

    //main thread queue(작업 영역)에 작업을 추가하는 핸들러
    // -> view에 직접적인 접근이 되지않아 간접동작을 위함
    val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            val time = msg.arg1
            val tv = msg.obj as TextView
            // 메인 ui를 건드는 작업.
            // 직접적으로 view 정보를 수정하는게 아니고 메인작업 queue에 작업추가. (간접적)

            tv.setText(time.toString())
        }
    }

    //시간 관련 쓰레드 만들기(oncreate에서 sleep 직접 동작시 어플자체가 중단)
    inner class TimerThread(val tv: TextView) : Thread() {
        // run() 메소드가 존재 : 쓰레드를 동작시키면 실행되는 메서드
        override fun run() {
            super.run()
            // 10 -> 0 (1초마다 1씩 감소)
            for (i in 10 downTo 0) {
                Log.d("타이머", i.toString())
                if(i==9){
                    playOrderAlarm()
                    Log.d("재생", i.toString())
                }
                //handler에게 정보를 전달해주는 객체.
                val message = Message()

                message.arg1 = i
                message.obj = tv

                handler.sendMessage(message)


                Thread.sleep(1000)

                if (i == 0) {
                    val tt = Intent(Intent.ACTION_CALL, Uri.parse("tel:12345678"))
                    startActivity(tt)
                }
            }

        }
    }


}