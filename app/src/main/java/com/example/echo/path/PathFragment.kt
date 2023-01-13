package com.example.echo.path

import android.Manifest
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.content.res.Resources.getSystem
import android.location.Location
import android.location.LocationManager

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.example.echo.R
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class PathFragment : Fragment() {
    private lateinit var mapView: MapView
    var frameLayout2: FrameLayout? = null




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

//        initMapView()
        val view = inflater.inflate(R.layout.fragment_path, container, false)


        mapView = view.findViewById(R.id.mapView)





            return view
        }



        override fun onDestroyView() {
            super.onDestroyView()
            mapView.onSurfaceDestroyed()
        }



}





