//package com.example.echo.path
//
//import android.Manifest
//import android.annotation.SuppressLint
//import android.app.Activity
//import android.content.ContentValues.TAG
//import android.content.Context.LOCATION_SERVICE
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.graphics.Color
//import android.location.Geocoder
//import android.location.Location
//import android.location.LocationListener
//import android.location.LocationManager
//import android.os.Bundle
//import android.os.Looper
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import android.widget.TextView
//import android.widget.Toast
//import androidx.core.app.ActivityCompat
//import androidx.core.view.isInvisible
//import androidx.fragment.app.Fragment
//import com.example.echo.R
//import com.google.android.gms.location.*
//import com.google.android.gms.maps.CameraUpdateFactory
//import com.google.android.gms.maps.GoogleMap
//import com.google.android.gms.maps.MapView
//import com.google.android.gms.maps.OnMapReadyCallback
//import com.google.android.gms.maps.model.LatLng
//import com.google.android.gms.maps.model.Marker
//import com.google.android.gms.maps.model.PolylineOptions
//import kotlinx.android.synthetic.main.activity_splash.*
//import java.text.SimpleDateFormat
//import java.util.*
//
//
//class MapFragment : Fragment(), OnMapReadyCallback {
//    private val googleMap: GoogleMap? = null
//    private lateinit var mView: MapView
//
//    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
//    lateinit var mLastLocation: Location
//    private var REQUEST_PERMISSION_LOCATION = 10
//    private var mLatitude: Double = 0.0
//    private var mLongitude: Double = 0.0
//    lateinit var manager : LocationManager
//    lateinit var locationListener : LocationListener
//    lateinit var myLocation : LatLng
//    lateinit var user_id : String
//    lateinit var coursedt : String
//    var open_yn : Boolean = false
//    var course_type : String = "A"
//    private var isTracking = false
//    private var pathPoints = mutableListOf<LatLng>()
//    private val currentMarker: Marker? = null
//    private val GPS_ENABLE_REQUEST_CODE = 2001
//    private val UPDATE_INTERVAL_MS = 1000 // 1초
//    private val FASTEST_UPDATE_INTERVAL_MS = 500 // 0.5초
//    private val PERMISSIONS_REQUEST_CODE = 100
//    var needRequest = false
//    var REQUIRED_PERMISSIONS = arrayOf<String>(
//        Manifest.permission.ACCESS_FINE_LOCATION,
//        Manifest.permission.ACCESS_COARSE_LOCATION
//    )
//
//    var mCurrentLocatiion: Location? = null
//    var currentPosition: LatLng? = null
//    private val mFusedLocationClient: FusedLocationProviderClient? = null
//    private val locationRequest: LocationRequest? = null
//    private val location: Location? = null
//    private val mLayout: View? = null
//    lateinit var tvMapCurrentTime : TextView
//    lateinit var tvMapTimer : TextView
//    //퍼미션 응답 처리 코드 (선언)
//    private val multiplePermissionsCode = 100
//
//
//    @SuppressLint("MissingInflatedId")
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//
//    ): View? {
//        // Inflate the layout for this fragment
//        val view = inflater.inflate(R.layout.fragment_map, container, false)
//        val btnMapRecordStart = view.findViewById<Button>(R.id.btnMapRecordStart)
//        val btnMapRecordEnd = view.findViewById<Button>(R.id.btnMapRecordEnd)
//        tvMapCurrentTime = view.findViewById(R.id.tvMapCurrentTime)
//        val tvMApCurrentLocation = view.findViewById<TextView>(R.id.tvMApCurrentLocation)
//        var tvMapTimer = view.findViewById<TextView>(R.id.tvMapTimer)
//        tvMapTimer.visibility = View.INVISIBLE
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
//
//
//
//        //경로저장하는 Array polilines
//        var polilines = emptyArray<LatLng>()
//
//        btnMapRecordEnd.visibility = View.INVISIBLE
//
//        btnMapRecordStart.setOnClickListener {
//            Log.d("산행중gps값받아오기","${mLatitude},${mLongitude}")
//            val polyLineOptions = PolylineOptions().width(5f).color(Color.BLUE)
//            /*polyLineOptions.add(myLocation)*/
//            tvMapTimer.visibility = View.VISIBLE
//            btnMapRecordStart.visibility = View.INVISIBLE
//            btnMapRecordEnd.visibility = View.VISIBLE
//
//        }
//
//
//        btnMapRecordEnd.setOnClickListener {
//
//            val intent = Intent(requireContext(), MapSaveActivity::class.java)
//            startActivity(intent)
//        }
//
//        mView = view.findViewById(R.id.map) as MapView
//        mView.onCreate(savedInstanceState)
//        mView.getMapAsync(this)
//
//        currentTime()
//
//
//        //주소 초기화
//        var address: List<String> = listOf("서울특별시", "중구", "명동")
//
//        // Got last known location. In some rare situations this can be null.
//        var geocoder = Geocoder(requireContext(), Locale.KOREA)
//        if (location != null) {
//            Toast.makeText(
//                requireContext(),
//                "현재위치..." + location.latitude + " / " + location.longitude,
//                Toast.LENGTH_SHORT
//            ).show()
//
//            val addrList =
//                geocoder.getFromLocation(location.latitude, location.longitude, 1)
//
//            for (addr in addrList) {
//                val splitedAddr = addr.getAddressLine(0).split(" ")
//                address = splitedAddr
//            }
//            //경기도, 성남시, 분당구, 삼평동
//            textView.append("현재위치 : ${address[1]}, ${address[2]}, ${address[3]}, ${address[4]}")
//        }
//
//                return view
//    }
//
//    //현재시간 구하기
//    fun currentTime() {
//
//        val currentTime = System.currentTimeMillis()
//        convertTimestampToDate(currentTime)
//
//    }
//
//    fun convertTimestampToDate(timestamp: Long) {
//        val time = SimpleDateFormat("yyyy년 MM월 dd일 hh시 mm분")
//        val date = time.format(timestamp)
//        tvMapCurrentTime.text = date
//         //2021년06월10일02시37분
//    }
//
//    private fun initLocationClient() {
//        fusedLocationProviderClient = context?.let {
//            LocationServices.getFusedLocationProviderClient(
//                it
//            )
//        }!!
//
//        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000).apply {
//            setMinUpdateDistanceMeters(1000F)
//            setGranularity(Granularity.GRANULARITY_FINE)
//            setWaitForAccurateLocation(true)
//        }.build()
//
//        val builder = LocationSettingsRequest.Builder()
//            .addLocationRequest(locationRequest!!)
//        val client = context?.let { LocationServices.getSettingsClient(it) }
//        val task = client?.checkLocationSettings(builder.build())
//        if (task != null) {
//            task.addOnSuccessListener {
//                Log.d(TAG, "location client setting success")
//            }
//        }
//
//        if (task != null) {
//            task.addOnFailureListener {
//                Log.d(TAG, "location client setting failure")
//            }
//        }
//    }
//
//
//
//
//
//    //Map실행
//    override fun onMapReady(googleMap: GoogleMap) {
//
//
//        //퍼미션 허가
//        if (ActivityCompat.checkSelfPermission( requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
//           ActivityCompat.requestPermissions( requireContext() as Activity, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION), 0 )
//            return
//        } else {
//
//            //현재위치 받아오기
//            fusedLocationProviderClient =
//                LocationServices.getFusedLocationProviderClient(requireContext())
//            val fusedLocationClient: FusedLocationProviderClient =
//                LocationServices.getFusedLocationProviderClient(requireContext())
//
//            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
//                if (location != null) {
//                    mLatitude = location.latitude
//                    mLongitude = location.longitude
//                    val currentlocation = LatLng(mLatitude, mLongitude)
//                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentlocation, 18F))
//                    Log.d("Test", "GPS Location Latitude: $mLatitude" + ", Longitude: $mLongitude")
//                }
//            }
//
//
//
//
//            locationListener = object : LocationListener {
//
//                val location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
//
//                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
//                    super.onStatusChanged(provider, status, extras)
//                    // provider의 상태가 변경될때마다 호출
//                    // deprecated
//                }
//
//                override fun onLocationChanged(location: Location) {
//
//
//
//                    mLatitude = location.latitude
//                    mLongitude = location.longitude
//                    myLocation = LatLng(mLatitude, mLongitude)
//                    Log.d("현재gps받아오기", "${location?.latitude},${location?.longitude}")
//
//                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 18F))
//                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation))
//
//                }
//
//                override fun onProviderEnabled(provider: String) {
//                    super.onProviderEnabled(provider)
//                }
//
//                override fun onProviderDisabled(provider: String) {
//                    super.onProviderDisabled(provider)
//                }
//            }
//            //업데이트
//            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000
//                , 10.0f, locationListener)
//
//        }
//    }
//
//
//
//    //권한 요청 결과 처리 함수
//    @SuppressLint("MissingSuperCall")
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String>, grantResults: IntArray
//    ) {
//        when (requestCode) {
//            multiplePermissionsCode -> {
//                if (grantResults.isNotEmpty()) {
//                    for ((i, permission) in permissions.withIndex()) {
//                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
//                            //권한 획득 실패시 동작
//                            Toast.makeText(
//                                requireContext(),
//                                "The user has denied to $permission",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                            Log.i("TAG", "I can't work for you anymore then. ByeBye!")
//                        } else {
//
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//
//    @SuppressLint("MissingPermission")
//    private fun checkLocationPermission() {
//        if (ActivityCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            fusedLocationProviderClient.lastLocation
//                .addOnSuccessListener { location: Location? ->
//                    // Got last known location. In some rare situations this can be null.
//                    var geocoder = Geocoder(requireContext(), Locale.KOREA)
//                    if (location != null) {
//                        Toast.makeText(
//                            requireContext(),
//                            "현재위치..." + location.latitude + " / " + location.longitude,
//                            Toast.LENGTH_SHORT
//                        ).show()
//
//                    }
//                }
//        } else {
//            Toast.makeText(requireContext(), "위치권한이 없습니다..", Toast.LENGTH_SHORT).show()
//
//        }
//    }
//
//    override fun onStart() {
//        super.onStart()
//        mView.onStart()
//    }
//
//    override fun onStop() {
//        super.onStop()
//        mView.onStop()
//    }
//
//    override fun onResume() {
//        super.onResume()
//        mView.onResume()
//
//    }
//
//    override fun onPause() {
//        super.onPause()
//        mView.onPause()
//    }
//
//    override fun onLowMemory() {
//        super.onLowMemory()
//        mView.onLowMemory()
//    }
//
//    override fun onDestroy() {
//        mView.onDestroy()
//        super.onDestroy()
//    }
//
//
//}
