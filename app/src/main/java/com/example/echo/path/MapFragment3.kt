package com.example.echo.path

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.example.echo.R

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapFragment3 : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mapView: MapView
    private lateinit var rootview: View

    lateinit var onConnectedListener: OnConnectedListener

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    var startLatitude = 0.0
    var startLongitude = 0.0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        rootview = inflater.inflate(R.layout.fragment_map3, container, false)
        mapView = rootview.findViewById(R.id.mapView2)
        mapView.onCreate(savedInstanceState)
        mapView.onResume()          //onResume()도 연결시켜야 정상적으로 맵이 보임.

        mapView.getMapAsync(this)



        return rootview

    }


    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap
        mMap.getUiSettings().setZoomControlsEnabled(true);
        initLocation()
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(startLatitude, startLongitude),15F)) //초기설정.KPU G동

        mMap.animateCamera(CameraUpdateFactory.zoomTo(10f))
        //  RecordMapActivity의 onConnect() 실행하여 맵 객체를 불러와 MapFragment에서 맵을 띄울 수 있게 해줌.
        onConnectedListener.onConnect(mMap)

        mMap.setOnMapLoadedCallback {
            try {
                mMap.isMyLocationEnabled = true   //현재위치표시 및 현재위치로 돌아올 수 있는 버튼 생성.
            } catch (e: SecurityException) {
            }
        }
    }

    interface OnConnectedListener {
        fun onConnect(mMap: GoogleMap)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // context -> RecordMapActivity. 프래그먼트에서 context 호출시 acitivity의 context가 불러짐.
        onConnectedListener = requireParentFragment() as OnConnectedListener
    }


    private fun initLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if(location == null) {
                    Log.e("test", "location get fail")
                } else {
                    Log.d("test", "${location.latitude} , ${location.longitude}")
                    startLatitude = location.latitude
                    startLongitude = location.longitude
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(startLatitude, startLongitude),15F)) //초기설정.KPU G동
                }
            }
            .addOnFailureListener {
                Log.e("test", "location error is ${it.message}")
                it.printStackTrace()
            }
    }



}


