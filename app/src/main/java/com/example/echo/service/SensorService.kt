package com.example.echo.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.*
import android.os.Process.THREAD_PRIORITY_BACKGROUND
import android.util.Log
import android.widget.Toast
import com.example.echo.CountActivity
import java.lang.Math.sqrt
import java.lang.Process


class SensorService : Service(), SensorEventListener {

    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null

    val TAG: String = "로그"

    private var accel: Float = 0.0f //초기
    private var accelCurrent: Float = 0.0f //이동하는 치수
    private var accelLast: Float = 0.0f

    //당장이 아니라 나중에 값을 넣겠다. 온크레이트 이후에 설정을 넣겠다.
    private lateinit var sensorManager: SensorManager

    // 메인 스레드로부터 메세지를 전달받을 핸들러 선언
    private inner class ServiceHandler(looper: Looper) : Handler(looper) {

        override fun handleMessage(msg: Message) {
            // 보통 서비스에서는 파일 다운로드 같은 작업을 수행함
            // 이 예제에서는 sleep. 5초를 주도록 함
            try {
                Thread.sleep(5000)
            } catch (e: InterruptedException) {
                // Restore interrupt status.
                Thread.currentThread().interrupt()
            }
            // 서비스를 사용하였다면 서비스를 종료해 주어야 함.
            // 아래 메소드는 작업 startId가 가장 최신일때만 서비스를 stop하게 함
            // 이렇게 하면 동시에 여러 작업할 때, 모든작업이 끝나야 stop이 된다
            // 이게뭔지는 이후 설명에서 나옴
            stopSelf(msg.arg1)
        }
    }

    override fun onCreate() {

        Log.d(TAG, "MainActivity - onCreate() called")
        this.sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        accel = 10f
        accelCurrent = SensorManager.GRAVITY_EARTH //지구 중력값 주기
        accelLast = SensorManager.GRAVITY_EARTH

        // 서비스는 메인스레드에서 동작하므로 서비스의 작업은 따로 스레드를 선언해 주어야 한다.
        // CPU priority를 background로 선언하여 ui의 버벅임을 방지해야 한다.
        HandlerThread("ServiceStartArguments").apply {
            start()

            // 스레드의 루퍼를 얻어와 핸들러를 만들어준다.
            serviceLooper = looper
            serviceHandler = ServiceHandler(looper)
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show()

        Log.d(TAG, "MainActivity - onResume() called")
        sensorManager.registerListener(this, sensorManager
            .getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            ,SensorManager.SENSOR_DELAY_NORMAL)

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        serviceHandler?.obtainMessage()?.also { msg ->
            msg.arg1 = startId
            serviceHandler?.sendMessage(msg)
        }

        // If we get killed, after returning from here, restart
        return START_STICKY
    }

    //흔들었을때 센서 감지
    override fun onSensorChanged(event: SensorEvent?) {
        Log.d(TAG, "MainActivity - onSensorChanged() called")

        val x:Float = event?.values?.get(0) as Float
        val y:Float = event?.values?.get(0) as Float
        val z:Float = event?.values?.get(0) as Float

        accelLast = accelCurrent
        accelCurrent = sqrt((x * x + y * y + z * z).toDouble()).toFloat()

        val delta: Float = accelCurrent - accelLast

        accel = accel * 0.9f + delta

        //액셀 치수가 30이 넘어가면 흔들었다고 휴대폰이 판단한다.
        if(accel > 30){
            Log.d(TAG, "흔들었다.")
            val test = Intent(applicationContext, CountActivity::class.java)
            startActivity(test)
        }
    }

    //센서에 정확도 변경되면 호출된다.
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d(TAG, "MainActivity - onAccuracyChanged() called")
    }

    override fun onBind(intent: Intent): IBinder? {
        // We don't provide binding, so return null
        return null
    }

    override fun onDestroy() {
        sensorManager.unregisterListener(this)
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show()
    }

}