package com.example.echo.path

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context.*
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.location.Location
import android.location.Location.distanceBetween
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.echo.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.android.synthetic.main.fragment_map.*
import java.util.*
import kotlin.concurrent.timer


private var mMap: GoogleMap? = null
private var REQUEST_ACCESS_FINE_LOCATION = 1000
private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
private lateinit var locationRequest: LocationRequest
//init_location
private lateinit var locationCallback: RecordMapFragment.MyLocationCallback
//선을 그어주는 변수
private val polylineOptions = PolylineOptions().width(7f).color(Color.RED)
//private  static String FRAGMENT_TAG = "FRAGMENTB_TAG"
private var detailTag: String = "DetailTag"
private var time = -1
private var timeTask: Timer? = null
val builder = LatLngBounds.builder()
//val extStorageDirectory: String =  Environment.getExternalStorageDirectory().toString()

// 기록 시작 버튼 클릭 여부 확인
var recordPressed = false
// 기록 시작 허가
var recordStart = false
// 이전 기록
var before_location = arrayOfNulls<Double>(2)
// 누적 거리
var total_distance: Double = 0.0
// 시간초
var total_sec: Int = 0

// 고도
var max_altitude: Double = 0.0
// 위도, 경도 저장
var latlngArray: ArrayList<Pair<Double, Double>> = ArrayList()
// 속도 저장
var averSpeed: String? = null

//만보기
var pedometer: Int = 0

var startLatLng: LatLng =  LatLng(0.0,0.0)

lateinit var tvMapTotalTime:TextView
lateinit var tvMapTotalDistance:TextView
lateinit var tvMapTotalAlt:TextView

class RecordMapFragment : Fragment(),    MapFragment3.OnConnectedListener,
    SensorEventListener {

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_ACCESS_FINE_LOCATION -> {
                if ((grantResults.isNotEmpty()) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    addLocationListener()
                } else {
                    Toast.makeText(requireContext(), "권한이 없습니다.", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        locationInit()

        val view = inflater.inflate(R.layout.fragment_record_map, container, false)

        val flMap = view.findViewById<FrameLayout>(R.id.flMap)
        val btnMapRecordStart2 = view.findViewById<Button>(R.id.btnMapRecordStart2)
        val btnMapRecordEnd2 = view.findViewById<Button>(R.id.btnMapRecordEnd2)
        tvMapTotalTime = view.findViewById(R.id.tvMapTotalTime)
        tvMapTotalDistance = view.findViewById(R.id.tvMapTotalDistance)
        tvMapTotalAlt = view.findViewById(R.id.tvMapTotalAlt)

        childFragmentManager.beginTransaction().replace(
            R.id.flMap,
            MapFragment3()
        ).commit()


        btnMapRecordEnd2.visibility = View.INVISIBLE
        btnMapRecordStart2.setOnClickListener {
            recordStart = true
            recordPressed = true
            btnMapRecordEnd2.visibility = View.VISIBLE
            btnMapRecordStart2.visibility = View.INVISIBLE
            Log.d("test-시작버튼클릭", recordStart.toString())
            startTimer()
        }
        btnMapRecordEnd2.setOnClickListener {
            recordStart = false
            recordPressed = false
            btnMapRecordStart2.visibility = View.VISIBLE
            btnMapRecordEnd2.visibility = View.INVISIBLE
            Log.d("test-종료버튼클릭", recordStart.toString())
            pauseTimer()
        }

//        var mapfr: Fragment = MapFragment()
//        var detailfr: Fragment = RecordFragment() //
        return view
    }

    override fun onResume() {
        super.onResume()
        permissionCheck(cancel = { showPermissionInfoDialog() },
            ok = { addLocationListener() })

    }

    override fun onPause() {    //onPause. 즉 액티비티가 보이지 않으면 실행되는 메소드.
        super.onPause()
    }

    // onDestroy에서 종료해줘야 Timer / LocationCallBack 이 정지됨.
    override fun onDestroy() {
        super.onDestroy()
        println("멈췄습니다")
        // onStop 호출때 마다 Timer / LocationCallBack 멈춰줌
        pauseTimer()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }


    //매인액티비티에서 위치정보를 얻고 난 뒤에 지도를 표시.
    private fun permissionCheck(cancel: () -> Unit, ok: () -> Unit) {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireContext() as Activity,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                cancel()
            } else {
                ActivityCompat.requestPermissions(
                    requireContext() as Activity,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_ACCESS_FINE_LOCATION
                )
            }
        } else {
            ok()
        }
    }


    private fun showPermissionInfoDialog() {
//        alert("현재 위치 정보를 얻기 위해서는 위치 권한이 필요합니다.", "권한이 필요한 이유") {
//            yesButton {
//                ActivityCompat.requestPermissions(
//                    this@RecordMapActivity,
//                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
//                    REQUEST_ACCESS_FINE_LOCATION
//                )
//            }
//            noButton { }
//        }.show()
    }

    @SuppressLint("MissingPermission")
    private fun addLocationListener() { //Init=false and record=true
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }


    private fun locationInit() {
        fusedLocationProviderClient = FusedLocationProviderClient(requireContext())
        locationCallback = MyLocationCallback()
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10000  //10000time 후에 현재위치로 돌아가는 옵션.
        locationRequest.fastestInterval = 5000

    }


    //타이머 측정 함수  TODO: period를 10으로 두면 백그라운드에서 엄청 느리게감. 이유찾아보기
    @SuppressLint("SetTextI18n")
    private fun startTimer() {
        timeTask = timer(period = 1000) {

            time++  //절대적 시간초.  //이 time을 가지고 속도를 구할수 있다.
            // 총 초시간 저장
            total_sec = time

            var sec = time % 60
            var min = time / 60 % 60
            var hour = time / 3600

            // 밑의 TextView.text 들이 null 을 입력받는 시점이 있음. 이 때 ?을 넣어줘야함.
            // thread에선 view 못 건드리므로 runOnUiThread 사용하여 변경
//            Activity().runOnUiThread {
////                //UI를 갱신해주는 쓰레드
////
////                secTextView?.text = sec.toString()
////                minTextView?.text = min.toString()
////                hourTextView?.text = hour.toString()
//                tvMapTotalTime.text = "${hour}:${min}:${sec}"
//
//            }

            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed(Runnable {
                tvMapTotalTime.text = "${hour}:${min}:${sec}"
            }, 0)
        }

    }

    private fun pauseTimer() {

        timeTask?.cancel()  //Timer의 객체로써 null일수 있기에 timetask 옆에 ? 붙음.
    }


    //TODO : start 누르는 순간 기록 시작. upload 누를 시 firestore에 업로드
    inner class MyLocationCallback : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult!!)

            val location = locationResult?.lastLocation
            //var circleOptions = CircleOptions()
            // let 은 객체를 블록의 인자로 넘겨서 it으로 사용 가능
            //run 은 객체를 블록의 리시버로 넘겨서 따로 객체 선언 없이 암시적으로 사용 가능.

            location?.run {

                val latLng = LatLng(
                    latitude,
                    longitude
                )    //interval되는 순간마다 latitud와 lonitude를 array배열로 저장해야함

                Log.d("MapActivity", "lan:$latitude, long:$longitude")


                //latitude,longitude를 builder에 넣어 나중에 모든 경로에 대해 알맞게 카메라 조정을 할 수 있음.
                builder.include(LatLng(latitude, longitude))

                if (recordPressed) {
                    recordStart = true
                    // 이전 기록 저장
                    before_location[0] = location!!.latitude
                    before_location[1] = location!!.longitude
                }

                if (recordStart) {

                    if (altitude > max_altitude)
                        max_altitude = altitude

                    println("고도: " + max_altitude)
                    //latitude,longitude를 builder에 넣어 나중에 모든 경로에 대해 알맞게 카메라 조정을 할 수 있음.
                    //builder.include(LatLng(latitude, longitude))
                    tvMapTotalAlt.text = max_altitude.toString()

                    // 위도, 경도 저장
                    latlngArray.add(Pair(latitude, longitude))

//                    val arrayex = FloatArray(1)
//                    distanceBetween(
//                        latitude,
//                        longitude,
//                        before_location[0]!!,
//                        before_location[1]!!,
//                        arrayex
//                    )
//
//                    total_distance += arrayex[0] / 1000
//                    tvMapTotalDistance.text = total_distance.toString()

                    // 거리 표시
//                    distanceKm.text = String.format("%.2f", total_distance)
                    // 시속 표시
//                    averSpeed = String.format("%.2f", total_distance * (3600 / total_sec))
//                    averageSpeed.text = averSpeed

//                    println("거리:" + total_distance)
//                    println("초:" + total_sec)
//                    println("시속: " + (total_distance * (3600 / total_sec)) + "km")


                    // Latlng(latitude,longitude) 을 이용하여 지도에 선 그리기
                    polylineOptions.add(latLng)
                    polylineOptions.width(13f)
                    polylineOptions.visible(true)   // 선이 보여질지/안보여질지 옵션.

                    mMap?.addPolyline(polylineOptions)
                    mMap?.moveCamera(CameraUpdateFactory.newLatLng(latLng))

                }
                Log.d("test-종료버튼클릭", recordStart.toString())

            }

        }
    }

    override fun onConnect(map: GoogleMap) {
        mMap = map
    }

    // UploadActivity에서 upload_success 신호를 보내면 finish()
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == AppCompatActivity.RESULT_OK)
            if (requestCode == 100) {
                if (data!!.getStringExtra("result").equals("upload_success"))
//                    finish()
                    Toast.makeText(requireContext(),"test-업로드", Toast.LENGTH_LONG).show()
            }
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        TODO("Not yet implemented")
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        TODO("Not yet implemented")
    }

}