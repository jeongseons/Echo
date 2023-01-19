package com.example.echo.path

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.echo.R
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker


class MapFragment2 : Fragment(), OnMapReadyCallback {


    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var googleMap: GoogleMap? = null
    private lateinit var mView: MapView
    private var currentMarker: Marker? = null
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0



    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.fragment_map2, container, false)
        mView = view.findViewById(R.id.map2) as MapView
        mView.onCreate(savedInstanceState)



         fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        fusedLocationClient.lastLocation.addOnSuccessListener { location : Location? ->
            if (location != null) {
                latitude = location.latitude
                longitude = location.longitude
                var currentlocation = LatLng(latitude, longitude)
                Log.d("Test1", "GPS Location Latitude: $latitude" +", Longitude: $longitude")

                googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentlocation, 15F))
                Log.d("카메라 Test1", "외않되")
                googleMap?.moveCamera(CameraUpdateFactory.newLatLng(currentlocation))
                val cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentlocation, 15F)
                googleMap?.moveCamera(cameraUpdate)

                Log.d("중심", "")


                setDefaultLocation(currentlocation)

            }else{

                Log.d("null Test2", "null값")
            }
        }
//        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000).apply {
//            setMinUpdateDistanceMeters(100.0F)
//            setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
//            setWaitForAccurateLocation(true)
//            setDefaultLocation()
//        }.build()



        mView.getMapAsync(this)
        moveCamera(googleMap, latitude, longitude)
        return view
    }



//    private fun initLocationClient() {
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
//
//        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000).apply {
//            setMinUpdateDistanceMeters(100.0F)
//            setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
//            setWaitForAccurateLocation(true)
//        }.build()
//
//
//    }

//    @SuppressLint("MissingPermission")
//    class LocationManager(
//        context: Context,
//        private var timeInterval: Long,
//        private var minimalDistance: Float
//    ) : LocationCallback() {
//
//        private var request: LocationRequest
//        private var locationClient: FusedLocationProviderClient
//
//        init {
//            // getting the location client
//            locationClient = LocationServices.getFusedLocationProviderClient(context)
//            request = createRequest()
//        }
//
//        private fun createRequest(): LocationRequest =
//            // New builder
//            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, timeInterval).apply {
//                setMinUpdateDistanceMeters(minimalDistance)
//                setGranularity(Granularity.GRANULARITY_COARSE)
//                setWaitForAccurateLocation(true)
//            }.build()
//
//        fun changeRequest(timeInterval: Long, minimalDistance: Float) {
//            this.timeInterval = timeInterval
//            this.minimalDistance = minimalDistance
//            createRequest()
//            stopLocationTracking()
//            startLocationTracking()
//        }
//
//        fun startLocationTracking() =
//            locationClient.requestLocationUpdates(request, this, Looper.getMainLooper())
//
//
//
//        fun stopLocationTracking() {
//            locationClient.flushLocations()
//            locationClient.removeLocationUpdates(this)
//        }
//
//        override fun onLocationResult(location: LocationResult) {
//           val result = LocationResult.PARCELABLE_WRITE_RETURN_VALUE.toString()
//            Log.d("뭐야", result)
//        }
//
//        override fun onLocationAvailability(availability: LocationAvailability) {
//            // TODO: react on the availability change
//        }
//
//    }




    override fun onMapReady(googleMap: GoogleMap) {

        Log.d("도착했니", "퍼미션허가")
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

    @SuppressLint("MissingPermission")
    fun setDefaultLocation(currentlocation : LatLng) {

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        val fusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                latitude = location.latitude
                longitude = location.longitude

                Log.d("Test3", "GPS Location Latitude: $latitude" + ", Longitude: $longitude")
                val currentlocation = LatLng(latitude, longitude)
                val cameraUpdate: CameraUpdate =
                    CameraUpdateFactory.newLatLngZoom(currentlocation, 15F)
                googleMap?.moveCamera(cameraUpdate)
                moveCamera(googleMap, latitude, longitude)
            }
        }


    }

    private fun moveCamera(googleMap: GoogleMap?, latitute: Double, longituge: Double) {
        googleMap?.let {
            it.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitute, longituge), 16f))
            Log.d("moveCamera Test4","왔니")
        }
    }

}