package com.example.echo

//import com.example.echo.path.MapFragment2

//import com.example.echo.path.MapFragment2
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.echo.board.BoardFragment
import com.example.echo.group.GroupFragment
import com.example.echo.myPage.MyCourseFragment
import com.example.echo.myPage.MyPageFragment
import com.example.echo.path.RecordMapFragment
import com.example.echo.service.SensorService
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kakao.sdk.common.util.Utility

private var backKeyPressedTime: Long = 0

class MainActivity : AppCompatActivity(), LifecycleObserver {
    var user_id = ""
    var moveCk = ""

    var check = false

    private var mSensorManager: SensorManager? = null
    private var mAccelerometer: Sensor? = null
    private var mShakeDetector: SensorService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

        val flMain = findViewById<FrameLayout>(R.id.flMain)
        val bnvMain = findViewById<BottomNavigationView>(R.id.bnvMain)


        //하단 메뉴바 아이콘 색상 틴트 빼기
        bnvMain.setItemIconTintList(null);
        val intent = Intent(applicationContext, SensorService::class.java)
        startService(intent)

        // ShakeDetector initialization
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mAccelerometer = mSensorManager!!
            .getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mShakeDetector = SensorService()
        mShakeDetector!!.setOnShakeListener(object : SensorService.OnShakeListener {
            override fun onShake(count: Int) {
                //감지시 할 작업 작성
                val intent = Intent(applicationContext, CountActivity::class.java)
                startActivity(intent)

                //백그라운드일시 포그라운드로 전환
                val am = getSystemService(ACTIVITY_SERVICE) as ActivityManager
                val tasks = am.getRunningTasks(100)
                if (!tasks.isEmpty()) {
                    val taskSize = tasks.size
                    for (i in 0 until taskSize) {
                        val taskInfo = tasks[i]
                        if (taskInfo.topActivity!!.packageName == packageName) {
                            check = true
                            am.moveTaskToFront(taskInfo.id, 0)
                        }
                    }
                }

            }
        })


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



    override fun onResume() {
        super.onResume()
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager!!.registerListener(
            mShakeDetector,
            mAccelerometer,
            SensorManager.SENSOR_DELAY_UI
        )
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


    fun changeFragment(index: Int){
        when(index){
            1 -> {
                supportFragmentManager.beginTransaction().replace(
                    R.id.flMain,
                    MyCourseFragment()
                ).commit()
            }

            2 -> {
                supportFragmentManager.beginTransaction().replace(
                    R.id.flMain,
                    MyPageFragment()
                ).commit()
            }
        }
    }

    override fun onBackPressed() {
        //super.onBackPressed();
        // 기존 뒤로 가기 버튼의 기능을 막기 위해 주석 처리 또는 삭제

        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간에 2.5초를 더해 현재 시간과 비교 후
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간이 2.5초가 지났으면 Toast 출력
        // 2500 milliseconds = 2.5 seconds
        if (System.currentTimeMillis() > backKeyPressedTime + 2500) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "뒤로 가기 버튼을 한 번 더 누르시면 종료됩니다.", Toast.LENGTH_LONG).show()
            return;
        }
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간에 2.5초를 더해 현재 시간과 비교 후
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간이 2.5초가 지나지 않았으면 종료
        if (System.currentTimeMillis() <= backKeyPressedTime + 2500) {
            finish();
            Toast.makeText(this,"이용해 주셔서 감사합니다.",Toast.LENGTH_LONG);
        }
    }

}
