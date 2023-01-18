package com.example.echo.path

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import com.example.echo.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.PolylineOptions
import java.text.SimpleDateFormat


class MapFragment : Fragment(), OnMapReadyCallback {

    private val googleMap: GoogleMap? = null
    private lateinit var mView: MapView
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    lateinit var mLastLocation: Location
    private var REQUEST_PERMISSION_LOCATION = 10
    private var mLatitude: Double = 0.0
    private var mLongitude: Double = 0.0
    lateinit var manager : LocationManager
    lateinit var locationListener : LocationListener
    lateinit var myLocation : LatLng
    lateinit var user_id : String
    lateinit var coursedt : String
    var open_yn : Boolean = false
    var course_type : String = "A"
    private var isTracking = false
    private var pathPoints = mutableListOf<LatLng>()
    private val currentMarker: Marker? = null
    private val GPS_ENABLE_REQUEST_CODE = 2001
    private val UPDATE_INTERVAL_MS = 1000 // 1초
    private val FASTEST_UPDATE_INTERVAL_MS = 500 // 0.5초
    private val PERMISSIONS_REQUEST_CODE = 100
    var needRequest = false
    var REQUIRED_PERMISSIONS = arrayOf<String>(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    var mCurrentLocatiion: Location? = null
    var currentPosition: LatLng? = null
    private val mFusedLocationClient: FusedLocationProviderClient? = null
    private val locationRequest: LocationRequest? = null
    private val location: Location? = null
    private val mLayout: View? = null
    lateinit var tvMapCurrentTime : TextView


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        val btnMapRecordStart = view.findViewById<Button>(R.id.btnMapRecordStart)
        val btnMapRecordEnd = view.findViewById<Button>(R.id.btnMapRecordEnd)
        tvMapCurrentTime = view.findViewById(R.id.tvMapCurrentTime)
        val tvMApCurrentLocation = view.findViewById<TextView>(R.id.tvMApCurrentLocation)



        //경로저장하는 Array polilines
        var polilines = emptyArray<LatLng>()

        btnMapRecordEnd.visibility = View.INVISIBLE

        btnMapRecordStart.setOnClickListener {
            Log.d("산행중gps값받아오기","${mLatitude},${mLongitude}")
            val polyLineOptions = PolylineOptions().width(5f).color(Color.BLUE)
            /*polyLineOptions.add(myLocation)*/

            btnMapRecordStart.visibility = View.INVISIBLE
            btnMapRecordEnd.visibility = View.VISIBLE

        }


        btnMapRecordEnd.setOnClickListener {

            val intent = Intent(requireContext(), MapSaveActivity::class.java)
            startActivity(intent)
        }

        mView = view.findViewById(R.id.map) as MapView
        mView.onCreate(savedInstanceState)
        mView.getMapAsync(this)

        main()

                return view
    }


    fun main() {

        val currentTime = System.currentTimeMillis()
        convertTimestampToDate(currentTime)

    }

    fun convertTimestampToDate(timestamp: Long) {
        val time = SimpleDateFormat("yyyy년 MM월 dd일 hh시 mm분")
        val date = time.format(timestamp)
        tvMapCurrentTime.text = date
         //2021년06월10일02시37분
    }


    //Map실행
    override fun onMapReady(googleMap: GoogleMap) {


        //퍼미션 허가
        if (ActivityCompat.checkSelfPermission( requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
           ActivityCompat.requestPermissions( requireContext() as Activity, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION), 0 )
            return
        } else {

            //현재위치 받아오기
            var manager = requireContext().getSystemService(LOCATION_SERVICE) as LocationManager
            locationListener = object : LocationListener {

                val location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                    super.onStatusChanged(provider, status, extras)
                    // provider의 상태가 변경될때마다 호출
                    // deprecated
                }

                override fun onLocationChanged(location: Location) {


                    mLatitude = location.latitude
                    mLongitude = location.longitude
                    myLocation = LatLng(mLatitude, mLongitude)
                    Log.d("현재gps받아오기", "${location?.latitude},${location?.longitude}")

                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 18F))
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation))
                    pathPoints.add(myLocation)
                }

                override fun onProviderEnabled(provider: String) {
                    super.onProviderEnabled(provider)
                }

                override fun onProviderDisabled(provider: String) {
                    super.onProviderDisabled(provider)
                }
            }
            //업데이트
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000
                , 10.0f, locationListener)

        }
    }


    override fun onStart() {
        super.onStart()
        mView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mView.onStop()
    }

    override fun onResume() {
        super.onResume()
        mView.onResume()

    }

    override fun onPause() {
        super.onPause()
        mView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mView.onLowMemory()
    }

    override fun onDestroy() {
        mView.onDestroy()
        super.onDestroy()
    }


}

