package com.example.echo.path

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.*
import android.location.LocationListener
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.echo.R
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class MapFragment2 : Fragment(), OnMapReadyCallback {


    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var googleMap: GoogleMap? = null
    private lateinit var mView: MapView
    private var currentMarker: Marker? = null
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    lateinit var manager : LocationManager
    lateinit var locationListener : LocationListener
    lateinit var tvMapCurrentTime2 : TextView
    lateinit var currentlocation : LatLng

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.fragment_map2, container, false)
        mView = view.findViewById(R.id.map2) as MapView
        mView.onCreate(savedInstanceState)
        mView.getMapAsync(this)
//        moveCamera(googleMap, latitude, longitude)
        currentlocation = LatLng(latitude, longitude)
        val geocoder = Geocoder(this.context)
        val address: List<Address>? = null

        val btnMapRecordStart2 = view.findViewById<Button>(R.id.btnMapRecordStart2)
        val btnMapRecordEnd2 = view.findViewById<Button>(R.id.btnMapRecordEnd2)
        tvMapCurrentTime2 = view.findViewById(R.id.tvMapCurrentTime2)
        val tvMApCurrentLocation2 = view.findViewById<TextView>(R.id.tvMapCurrentLocation2)
        var tvMapTimer = view.findViewById<TextView>(R.id.tvMapTimer2)
//        tvMapTimer.visibility = View.INVISIBLE
        btnMapRecordEnd2.visibility = View.INVISIBLE


        btnMapRecordStart2.setOnClickListener {
            Log.d("?????????gps???????????????","${latitude},${longitude}")
            val polyLineOptions = PolylineOptions().width(5f).color(Color.BLUE)
            /*polyLineOptions.add(myLocation)*/
//            tvMapTimer.visibility = View.VISIBLE
            btnMapRecordStart2.visibility = View.INVISIBLE
            btnMapRecordEnd2.visibility = View.VISIBLE

//            createLocationRequest()

        }

        btnMapRecordEnd2.setOnClickListener {

            val intent = Intent(requireContext(), MapSaveActivity::class.java)
            startActivity(intent)
        }

        currentTime()
        tvMApCurrentLocation2.text = getCurrentAddress(currentlocation).toString()






        return view
    }



    //???????????? ?????????
    fun currentTime() {

        val currentTime = System.currentTimeMillis()
        convertTimestampToDate(currentTime)

    }

    fun convertTimestampToDate(timestamp: Long) {
        val time = SimpleDateFormat("yyyy??? MM??? dd??? hh??? mm???")
        val date = time.format(timestamp)
        tvMapCurrentTime2.text = date
        //2021???06???10???02???37???
    }

    //??????, ????????? ?????? ?????????




    override fun onMapReady(googleMap: GoogleMap) {

        Log.d("????????????", "???????????????")
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireContext() as Activity,
                arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION),
                0
            )

        } else {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
            val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

            fusedLocationClient.lastLocation.addOnSuccessListener { location : Location? ->
                if (location != null) {
                    latitude = location.latitude
                    longitude = location.longitude
                    currentlocation = LatLng(latitude, longitude)
                    Log.d("????????? onMapReady Test", "GPS Location Latitude: $latitude" +", Longitude: $longitude")

                    googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentlocation, 19F))

                    googleMap?.moveCamera(CameraUpdateFactory.newLatLng(currentlocation))
//                    val cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentlocation, 20F)
//                    googleMap?.moveCamera(cameraUpdate)
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(currentlocation)
                            .title("?????? ??????")
                    )

                    googleMap.isMyLocationEnabled = true
                    googleMap.uiSettings.apply{
                        isCompassEnabled = true
                    }


                }else{

                    Log.d("null Test2", "null???")
                }
            }
        }


    }

//    fun createLocationRequest() {
//        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000).apply {
//            setMinUpdateDistanceMeters(1000F)
//            setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
//            setWaitForAccurateLocation(true)
//        }.build()
//        Log.d("???????????? Test", "${currentlocation.latitude}, ${currentlocation.longitude}")
//    }
//





    fun getCurrentAddress(currentlocation : LatLng): String? {

        //????????????... GPS??? ????????? ??????
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses: List<Address>?
        addresses = try {
            geocoder.getFromLocation(
                currentlocation.latitude,
                currentlocation.longitude,
                1
            )
        } catch (ioException: IOException) {
            //???????????? ??????
            Toast.makeText(requireContext(), "???????????? ????????? ????????????", Toast.LENGTH_LONG).show()
            return "???????????? ????????? ????????????"
        } catch (illegalArgumentException: IllegalArgumentException) {
            Toast.makeText(requireContext(), "????????? GPS ??????", Toast.LENGTH_LONG).show()
            return "????????? GPS ??????"
        }
        return if (addresses == null || addresses.size == 0) {
            Toast.makeText(requireContext(), "?????? ?????????", Toast.LENGTH_LONG).show()
            "?????? ?????????"
        } else {
            val address = addresses[0]
            address.getAddressLine(0).toString()
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

typealias Polylines = MutableList<Polyline>

typealias Polyline = MutableList<LatLng>