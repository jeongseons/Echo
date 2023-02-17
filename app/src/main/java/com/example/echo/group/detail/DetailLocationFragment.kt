package com.example.echo.group.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.echo.R
import com.example.echo.group.GroupActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

private lateinit var map: GoogleMap
lateinit var mapDetailLocation: MapView

class DetailLocationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_detail_location, container, false)
        val activity = activity as GroupActivity
        val btnDetailLocPrev = view.findViewById<Button>(R.id.btnDetailLocPrev)

        mapDetailLocation = view.findViewById<MapView>(R.id.mapDetailLocation)
        mapDetailLocation.onCreate(savedInstanceState)
        mapDetailLocation.onResume()

        val location : LatLng = LatLng(35.14986000000000,126.91983000000000)
        initMap(location)

        btnDetailLocPrev.setOnClickListener {
            activity.changeFragment(DetailTalkFragment())
        }

        return view
    }

    fun initMap(location: LatLng){

        mapDetailLocation.getMapAsync {

            map = it
            map.getUiSettings().setZoomControlsEnabled(true);

            map.setOnMapLoadedCallback {
                try {
                    map.isMyLocationEnabled = true   //현재위치표시 및 현재위치로 돌아올 수 있는 버튼 생성.
                } catch (e: SecurityException) {
                }
            }

            map!!.moveCamera(CameraUpdateFactory.newLatLng(LatLng(location.latitude, location.longitude)))
            map!!.animateCamera(CameraUpdateFactory.zoomTo(15f))

            map!!.addMarker(
                MarkerOptions()
                    .position(LatLng(location.latitude, location.longitude))
                    .title("현재 위치")
            )

        }

    }


}