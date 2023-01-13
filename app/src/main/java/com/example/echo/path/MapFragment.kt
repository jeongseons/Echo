package com.example.echo.path

import android.app.Activity
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.location.LocationRequest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.echo.R
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions


class MapFragment : Fragment(), OnMapReadyCallback {

    private val googleMap: GoogleMap? = null
    private var mLocationManager: LocationManager? = null
    private var mLocationListener: LocationListener? = null
    private lateinit var mView: MapView
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    lateinit var mLastLocation: Location
    private lateinit var mLocationRequest: LocationRequest
    private var REQUEST_PERMISSION_LOCATION = 10
    private var mLatitude: Double = 0.0
    private var mLongitude: Double = 0.0
    lateinit var imgMapRecordStart : ImageView

    lateinit var manager : LocationManager
    lateinit var locationListener : LocationListener
    lateinit var myLocation : LatLng


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        imgMapRecordStart = view.findViewById(R.id.imgMapRecordStart)
//        val imgMapCurrentLocation = view.findViewById<ImageView>(R.id.imgMapCurrentLocation)


        mView = view.findViewById(R.id.map) as MapView
        mView.onCreate(savedInstanceState)
        mView.getMapAsync(this)

        mFusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())


// Instantiates a new Polyline object and adds points to define a rectangle
        val polylineOptions = PolylineOptions().width(5f).color(Color.BLUE)
            .add(LatLng(35.11049333333334, 126.87776166666666))
            .add(LatLng(34.14671833333333, 125.92025333333335)) // North of the previous point, but at the same longitude

        googleMap?.addPolyline(polylineOptions)


        imgMapRecordStart.setOnClickListener {

            val polyLineOptions = PolylineOptions().width(5f).color(Color.BLUE)
            var testCourese = LatLng(35.11049333333334,126.87776166666666)
            var testCourese2 = LatLng(35.14671833333333,126.92025333333335)
            /*polyLineOptions.add(myLocation)*/
            polyLineOptions.add(testCourese)
            polyLineOptions.add(testCourese2)
            googleMap?.addPolyline(polyLineOptions)
//            Log.d("산행중 gps값 받아오기","${mLatitude},${mLongitude}")
        }

//        imgMapCurrentLocation.setOnClickListener {
//            googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 19F))
//            googleMap?.moveCamera(CameraUpdateFactory.newLatLng(myLocation))
//        }

                return view

    }


    //Map실행
    override fun onMapReady(googleMap: GoogleMap) {
        //퍼미션 허가
        if (ActivityCompat.checkSelfPermission(
                requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireContext() as Activity,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                0
            )
        } else {
            //현재위치 받아오기
            manager = requireContext().getSystemService(LOCATION_SERVICE) as LocationManager
            locationListener = object : LocationListener{
                override fun onLocationChanged(location: Location) {



                    mLatitude = location.latitude
                    mLongitude = location.longitude
                    myLocation = LatLng(mLatitude, mLongitude)
                    googleMap.addMarker(MarkerOptions().position(myLocation).title("현재위치"))
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 19F))
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation))
                    Log.d("현재 gps 받아오기","${location?.latitude},${location?.longitude}")

                }


                override fun onProviderEnabled(provider: String) {
                    super.onProviderEnabled(provider)
                }

                override fun onProviderDisabled(provider: String) {
                    super.onProviderDisabled(provider)
                }
            }
            //업데이트
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                10_000L,10f,locationListener)
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

