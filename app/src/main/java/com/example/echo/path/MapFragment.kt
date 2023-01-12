package com.example.echo.path

import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.echo.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.LocationSource
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng


class MapFragment : Fragment(), OnMapReadyCallback {

    private val googleMap: GoogleMap? = null
    private lateinit var mView: MapView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
      val view =  inflater.inflate(R.layout.fragment_map, container, false)



        mView = view.findViewById(R.id.map) as MapView
        mView.onCreate(savedInstanceState)
        mView.getMapAsync(this)










        return view

    }



    override fun onMapReady(googleMap: GoogleMap) {
        val myLocation = LatLng(35.149400832761, 126.91948924905)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 19F))
    }

    fun getLocation() {
//        googleMap?.setLocationSource(LocationSource)
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
//        if (requestingLocationUpdates) startLocationUpdates()
        mView.onResume()
    }

    private fun startLocationUpdates() {
//        fusedLocationClient.requestLocationUpdates(locationRequest,
//            locationCallback,
//            Looper.getMainLooper())
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