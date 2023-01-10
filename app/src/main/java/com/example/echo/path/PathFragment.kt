package com.example.echo.path

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.example.echo.R
import net.daum.mf.map.api.MapView

class PathFragment : Fragment() {
    private lateinit var mapView:MapView
    var frameLayout2: FrameLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

//        initMapView()
        val view =  inflater.inflate(R.layout.fragment_path, container, false)

        mapView = view.findViewById(R.id.mapView)


        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onSurfaceDestroyed()
    }


}


