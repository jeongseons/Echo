package com.example.echo.path

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.echo.R
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar


class MapFragment2 : Fragment(), OnMapReadyCallback {


    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var googleMap: GoogleMap? = null
    private lateinit var mView: MapView
    private var currentMarker: Marker? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.fragment_map2, container, false)
        mView = view.findViewById(R.id.map2) as MapView
        mView.onCreate(savedInstanceState)



         fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000).apply {
            setMinUpdateDistanceMeters(100.0F)
            setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
            setWaitForAccurateLocation(true)
            setDefaultLocation()
        }.build()


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        mView.getMapAsync(this)

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

    @SuppressLint("MissingPermission")
    class LocationManager(
        context: Context,
        private var timeInterval: Long,
        private var minimalDistance: Float
    ) : LocationCallback() {

        private var request: LocationRequest
        private var locationClient: FusedLocationProviderClient

        init {
            // getting the location client
            locationClient = LocationServices.getFusedLocationProviderClient(context)
            request = createRequest()
        }

        private fun createRequest(): LocationRequest =
            // New builder
            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, timeInterval).apply {
                setMinUpdateDistanceMeters(minimalDistance)
                setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
                setWaitForAccurateLocation(true)
            }.build()

        fun changeRequest(timeInterval: Long, minimalDistance: Float) {
            this.timeInterval = timeInterval
            this.minimalDistance = minimalDistance
            createRequest()
            stopLocationTracking()
            startLocationTracking()
        }

        fun startLocationTracking() =
            locationClient.requestLocationUpdates(request, this, Looper.getMainLooper())



        fun stopLocationTracking() {
            locationClient.flushLocations()
            locationClient.removeLocationUpdates(this)
        }

        override fun onLocationResult(location: LocationResult) {
           val result = LocationResult.PARCELABLE_WRITE_RETURN_VALUE.toString()
            Log.d("뭐야", result)
        }

        override fun onLocationAvailability(availability: LocationAvailability) {
            // TODO: react on the availability change
        }

    }




    override fun onMapReady(googleMap: GoogleMap) {
        Log.d("도착했니", "여기")
        if (ActivityCompat.checkSelfPermission( requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(
                requireContext() as Activity, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION),0)
            setDefaultLocation();

            return
        }else{
           Log.d("스낵바", "여기")
           Snackbar.make(View(requireContext()), "이 앱을 실행하려면 위치 접근 권한이 필요합니다." , Snackbar.LENGTH_LONG).show()
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

    fun setDefaultLocation() {


        //디폴트 위치, Seoul
        val DEFAULT_LOCATION = LatLng(37.56, 126.97)



        val cameraUpdate: CameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15F)
        googleMap?.moveCamera(cameraUpdate)
    }


}