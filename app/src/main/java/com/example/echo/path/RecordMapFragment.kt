package com.example.echo.path

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Context.*
import android.content.DialogInterface
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
import com.example.echo.board.BoardWriteActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
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
private var time = -1
private var timeTask: Timer? = null
val builder = LatLngBounds.builder()

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

var startLatLng =  LatLng(0.0,0.0)

lateinit var tvMapTotalTime:TextView
lateinit var tvMapTotalDistance:TextView
lateinit var tvMapTotalAlt:TextView

var startCk = false


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
        val btnMapRecordPause = view.findViewById<Button>(R.id.btnMapRecordPause)
        tvMapTotalTime = view.findViewById(R.id.tvMapTotalTime)
        tvMapTotalDistance = view.findViewById(R.id.tvMapTotalDistance)
        tvMapTotalAlt = view.findViewById(R.id.tvMapTotalAlt)

        childFragmentManager.beginTransaction().replace(
            R.id.flMap,
            MapFragment3()
        ).commit()

        btnMapRecordStart2.visibility = View.VISIBLE
        btnMapRecordPause.visibility = View.INVISIBLE

        //기록시작
        btnMapRecordStart2.setOnClickListener {
            startCk = true
            recordStart = true
            recordPressed = true
            btnMapRecordPause.visibility = View.VISIBLE
            btnMapRecordStart2.visibility = View.INVISIBLE
            Log.d("test-시작버튼클릭", recordStart.toString())
            startTimer()
        }
        //일시정지
        btnMapRecordPause.setOnClickListener {
            recordStart = false
            recordPressed = false
            btnMapRecordStart2.visibility = View.VISIBLE
            btnMapRecordPause.visibility = View.INVISIBLE
            Log.d("test-종료버튼클릭", recordStart.toString())
            pauseTimer()
        }
        //기록중단 - 저장
        btnMapRecordEnd2.setOnClickListener {
            recordStart = false
            recordPressed = false
            Log.d("test-종료버튼클릭", recordStart.toString())
            pauseTimer()
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            if(latlngArray.size == 0){
                Toast.makeText(context,"아직 이동하지 않으셨습니다", Toast.LENGTH_LONG).show()
            }else {
                val intent = Intent(requireContext(), MapSaveActivity::class.java)
                intent.putExtra("latlngArray",latlngArray)
                intent.putExtra("totalTime",tvMapTotalTime.text)
                intent.putExtra("totalAlt",tvMapTotalAlt.text)
                intent.putExtra("totalDistance",tvMapTotalDistance.text)
                intent.putExtra("speed", "${averSpeed}km/h")
                startActivity(intent)
            }
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
        val dialog: AlertDialog.Builder = AlertDialog.Builder(context,
            android.R.style.ThemeOverlay_Material_Dialog_Alert
        )
        dialog.setMessage("위치정보 권한 승인")
            .setTitle("현재 위치 정보를 얻기 위해서는 위치 권한이 필요합니다")
            .setPositiveButton("아니오", DialogInterface.OnClickListener { dialog, which ->

            })
            .setNeutralButton("예",
                DialogInterface.OnClickListener { dialog, which ->
                    ActivityCompat.requestPermissions(
                        requireContext() as Activity,
                        arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_ACCESS_FINE_LOCATION
                    )
                })
            .show()
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
    private fun startTimer() {
        timeTask = timer(period = 1000) {

            time++  //절대적 시간초.  //이 time을 가지고 속도를 구할수 있다.
            // 총 초시간 저장
            total_sec = time

            var sec = (time % 60).toString()
            var min = (time / 60 % 60).toString()
            var hour = (time / 3600).toString()

            if(sec.length<2){
                sec = "0${sec}"
            }

            if(min.length<2){
                min = "0${min}"
            }

            if(hour.length<2){
                hour = "0${hour}"
            }

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

            if(recordPressed){
            location?.run {

                val latLng = LatLng(
                    latitude,
                    longitude
                )    //interval되는 순간마다 latitud와 lonitude를 array배열로 저장해야함

                Log.d("MapActivity", "lan:$latitude, long:$longitude")

                if(startCk){
                    startLatLng = LatLng(latitude, longitude)
                    before_location[0] = startLatLng.latitude
                    before_location[1] = startLatLng.longitude
                    mMap?.moveCamera(CameraUpdateFactory.newLatLng(startLatLng))
                    mMap?.addMarker(
                        MarkerOptions()
                        .position(startLatLng)
                        .title("출발지점"))
                    startCk = false
                }

                //latitude,longitude를 builder에 넣어 나중에 모든 경로에 대해 알맞게 카메라 조정을 할 수 있음.
                builder.include(LatLng(latitude, longitude))


                Log.d("test-위치좌표", before_location.toString())

                if (recordStart) {

                    if (altitude > max_altitude)
                        max_altitude = altitude

                    println("고도: " + max_altitude)
                    //latitude,longitude를 builder에 넣어 나중에 모든 경로에 대해 알맞게 카메라 조정을 할 수 있음.
                    //builder.include(LatLng(latitude, longitude))
                    tvMapTotalAlt.text = "${max_altitude.toString()}km"

                    // 위도, 경도 저장
                    latlngArray.add(Pair(latitude, longitude))

                    val arrayex = FloatArray(1)
                    distanceBetween(
                        latitude,
                        longitude,
                        before_location[0]!!,
                        before_location[1]!!,
                        arrayex
                    )
//
                    total_distance += arrayex[0] / 1000
                    // 거리 표시
                    tvMapTotalDistance.text = "${String.format("%.2f", total_distance)}km"

                    // 시속 표시
//                    averSpeed = String.format("%.2f", total_distance * (3600 / total_sec))
//                    averageSpeed.text = averSpeed

//                    println("거리:" + total_distance)
//                    println("초:" + total_sec)
//                    println("시속: " + (total_distance * (3600 / total_sec)) + "km")


                    // Latlng(latitude,longitude) 을 이용하여 지도에 선 그리기
                    if(!startCk) {
                        polylineOptions.add(latLng)
                        polylineOptions.width(13f)
                        polylineOptions.visible(true)   // 선이 보여질지/안보여질지 옵션.

                        mMap?.addPolyline(polylineOptions)
                    }
                    mMap?.moveCamera(CameraUpdateFactory.newLatLng(latLng))

                }
                Log.d("test-기록버튼클릭", recordStart.toString())
                Log.d("test-기록중", latlngArray.toString())
            }

            if (recordPressed) {
                recordStart = true
                // 이전 기록 저장
                before_location[0] = location!!.latitude
                before_location[1] = location!!.longitude
            }

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