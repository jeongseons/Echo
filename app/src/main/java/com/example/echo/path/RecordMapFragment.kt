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
import android.location.Geocoder
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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.timer


private var mMap: GoogleMap? = null
private var REQUEST_ACCESS_FINE_LOCATION = 1000
private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
private lateinit var locationRequest: LocationRequest
//init_location
private lateinit var locationCallback: RecordMapFragment.MyLocationCallback
//?????? ???????????? ??????
private val polylineOptions = PolylineOptions().width(7f).color(Color.RED)
private var time = -1
private var timeTask: Timer? = null
val builder = LatLngBounds.builder()

// ?????? ?????? ?????? ?????? ?????? ??????
var recordPressed = false
// ?????? ?????? ??????
var recordStart = false
// ?????? ??????
var before_location = arrayOfNulls<Double>(2)
// ?????? ??????
var total_distance: Double = 0.0
// ?????????
var total_sec: Int = 0

// ??????
var max_altitude: Double = 0.0
// ??????, ?????? ??????
var latlngArray: ArrayList<Pair<Double, Double>> = ArrayList()
// ?????? ??????
var averSpeed: String? = null

//?????????
var pedometer: Int = 0

var startLatLng =  LatLng(0.0,0.0)

var startTime = ""
var endTime = ""

lateinit var tvMapTotalTime:TextView
lateinit var tvMapTotalDistance:TextView
lateinit var tvMapTotalAlt:TextView
lateinit var tvMapCurrentLocation2:TextView
lateinit var tvMapCurrentTime2:TextView

var startCk = false
var startFirst = true


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
                    Toast.makeText(requireContext(), "????????? ????????????.", Toast.LENGTH_LONG).show()
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
        tvMapCurrentLocation2 = view.findViewById<TextView>(R.id.tvMapCurrentLocation2)
        tvMapTotalTime = view.findViewById(R.id.tvMapTotalTime)
        tvMapTotalDistance = view.findViewById(R.id.tvMapTotalDistance)
        tvMapTotalAlt = view.findViewById(R.id.tvMapTotalAlt)
        tvMapCurrentTime2 = view.findViewById(R.id.tvMapCurrentTime2)

        childFragmentManager.beginTransaction().replace(
            R.id.flMap,
            MapFragment3()
        ).commit()

        btnMapRecordStart2.visibility = View.VISIBLE
        btnMapRecordPause.visibility = View.INVISIBLE

        //????????????
        btnMapRecordStart2.setOnClickListener {
            startCk = true
            recordStart = true
            recordPressed = true
            btnMapRecordPause.visibility = View.VISIBLE
            btnMapRecordStart2.visibility = View.INVISIBLE
            Log.d("test-??????????????????", recordStart.toString())
            startTimer()
        }
        //????????????
        btnMapRecordPause.setOnClickListener {
            recordStart = false
            recordPressed = false
            btnMapRecordStart2.visibility = View.VISIBLE
            btnMapRecordPause.visibility = View.INVISIBLE
            Log.d("test-??????????????????", recordStart.toString())
            pauseTimer()
        }
        //???????????? - ??????
        btnMapRecordEnd2.setOnClickListener {
            recordStart = false
            recordPressed = false
            Log.d("test-??????????????????", recordStart.toString())
            pauseTimer()
            endTime = getNowTime()
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
//            if(latlngArray.size == 0){
//                Toast.makeText(context,"?????? ???????????? ??????????????????", Toast.LENGTH_LONG).show()
//            }else {
                val intent = Intent(requireContext(), MapSaveActivity::class.java)
                intent.putExtra("latlngArray",latlngArray)
                intent.putExtra("totalTime",tvMapTotalTime.text)
                intent.putExtra("totalAlt",tvMapTotalAlt.text)
                intent.putExtra("totalDistance",tvMapTotalDistance.text)
                intent.putExtra("startTime",startTime)
                intent.putExtra("endTime",endTime)
                intent.putExtra("speed", "${averSpeed}km/h")
                startActivity(intent)
//            }
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        permissionCheck(cancel = { showPermissionInfoDialog() },
            ok = { addLocationListener() })

    }

    override fun onPause() {    //onPause. ??? ??????????????? ????????? ????????? ???????????? ?????????.
        super.onPause()
    }

    // onDestroy?????? ??????????????? Timer / LocationCallBack ??? ?????????.
    override fun onDestroy() {
        super.onDestroy()
        println("???????????????")
        // onStop ????????? ?????? Timer / LocationCallBack ?????????
        pauseTimer()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }


    //???????????????????????? ??????????????? ?????? ??? ?????? ????????? ??????.
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
        dialog.setMessage("???????????? ?????? ??????")
            .setTitle("?????? ?????? ????????? ?????? ???????????? ?????? ????????? ???????????????")
            .setPositiveButton("?????????", DialogInterface.OnClickListener { dialog, which ->

            })
            .setNeutralButton("???",
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
        locationRequest.interval = 10000  //10000time ?????? ??????????????? ???????????? ??????.
        locationRequest.fastestInterval = 5000

    }


    //????????? ?????? ??????  TODO: period??? 10?????? ?????? ????????????????????? ?????? ????????????. ??????????????????
    private fun startTimer() {
        timeTask = timer(period = 1000) {

            time++  //????????? ?????????.  //??? time??? ????????? ????????? ????????? ??????.
            // ??? ????????? ??????
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

        timeTask?.cancel()  //Timer??? ???????????? null?????? ????????? timetask ?????? ? ??????.
    }

    fun getNowTime(): String {
        var now = System.currentTimeMillis();
        var date = Date(now);
        var simpleDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")

        return simpleDateFormat.format(date)
    }

    //TODO : start ????????? ?????? ?????? ??????
    inner class MyLocationCallback : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult!!)

            val location = locationResult?.lastLocation

            if(recordPressed){
            location?.run {

                val latLng = LatLng(
                    latitude,
                    longitude
                )    //interval?????? ???????????? latitud??? lonitude??? array????????? ???????????????

                Log.d("MapActivity", "lan:$latitude, long:$longitude")

                if(startCk){
                    startTime = getNowTime()
                    tvMapCurrentTime2.text = startTime
                    startLatLng = LatLng(latitude, longitude)
                    val geocoder = Geocoder(context)

                    try {
                        var addr = geocoder.getFromLocation(latitude.toDouble(), longitude.toDouble(), 1).first().adminArea
                        if(geocoder.getFromLocation(latitude.toDouble(), longitude.toDouble(), 1).first().subLocality==null) {
                            var addr3 = geocoder.getFromLocation(latitude.toDouble(), longitude.toDouble(), 1).first().locality
                            tvMapCurrentLocation2.text= "${addr} ${addr3}"
                        }
                        else{
                            var addr2 = geocoder.getFromLocation(latitude.toDouble(), longitude.toDouble(), 1).first().subLocality
                            tvMapCurrentLocation2.text = "${addr} ${addr2}"
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    before_location[0] = startLatLng.latitude
                    before_location[1] = startLatLng.longitude
                    mMap?.moveCamera(CameraUpdateFactory.newLatLng(startLatLng))
                    if(startFirst) {
                        mMap?.addMarker(
                            MarkerOptions()
                                .position(startLatLng)
                                .title("????????????")
                        )
                        startFirst = false
                    }
                    startCk = false
                }

                //latitude,longitude??? builder??? ?????? ????????? ?????? ????????? ?????? ????????? ????????? ????????? ??? ??? ??????.
                builder.include(LatLng(latitude, longitude))

                Log.d("test-????????????", before_location.toString())

                if (recordStart) {

                    if (altitude > max_altitude)
                        max_altitude = altitude

                    println("??????: " + max_altitude)

                    tvMapTotalAlt.text = "${String.format("%.2f", max_altitude/10)}m"
                    // ??????, ?????? ??????
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
                    // ?????? ??????
                    tvMapTotalDistance.text = "${String.format("%.2f", total_distance)}km"

                    // ?????? ??????
//                    averageSpeed.text = averSpeed
//                    averSpeed = String.format("%.2f", total_distance * (3600 / total_sec))

//                    println("??????:" + total_distance)
//                    println("???:" + total_sec)
//                    println("??????: " + (total_distance * (3600 / total_sec)) + "km")


                    // Latlng(latitude,longitude) ??? ???????????? ????????? ??? ?????????
                    if(!startCk) {
                        polylineOptions.add(latLng)
                        polylineOptions.width(13f)
                        polylineOptions.visible(true)   // ?????? ????????????/??????????????? ??????.

                        mMap?.addPolyline(polylineOptions)
                        mMap?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                    }

                }
                Log.d("test-??????????????????", recordStart.toString())
                Log.d("test-?????????", latlngArray.toString())
            }

            if (recordPressed) {
                recordStart = true
                // ?????? ?????? ??????
                before_location[0] = location!!.latitude
                before_location[1] = location!!.longitude
            }

        }
            }
    }

    override fun onConnect(map: GoogleMap) {
        mMap = map
    }

    // UploadActivity?????? upload_success ????????? ????????? finish()
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == AppCompatActivity.RESULT_OK)
            if (requestCode == 100) {
                if (data!!.getStringExtra("result").equals("upload_success"))
//                    finish()
                    Toast.makeText(requireContext(),"test-?????????", Toast.LENGTH_LONG).show()
            }
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        TODO("Not yet implemented")
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        TODO("Not yet implemented")
    }

}