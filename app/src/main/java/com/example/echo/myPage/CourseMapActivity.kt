package com.example.echo.myPage

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.echo.R
import com.example.echo.path.MapVO
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions


var mapList = ArrayList<MapVO>()
private lateinit var map: GoogleMap
private val polylineOptions = PolylineOptions().width(7f).color(Color.RED)
lateinit var mapCourseMapFull: MapView

class CourseMapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_map)

        mapList = intent.getSerializableExtra("mapList") as ArrayList<MapVO>
        Log.d("test-mapList", mapList.toString())
        mapCourseMapFull = findViewById(R.id.mapCourseMapFull)
        mapCourseMapFull.onCreate(savedInstanceState)
        mapCourseMapFull.onResume()

        initMap(mapList)
//        mapCourseMapFull.getMapAsync(this)
//
//        if(mapList.size>=1) {
//            settingMap(mapList)
//       }

    }

//    override fun onMapReady(googleMap: GoogleMap) {
//
//        mapList = intent.getSerializableExtra("mapList") as ArrayList<MapVO>
//        Log.d("test-mapList", mapList.toString())
//
//        map!!.moveCamera(CameraUpdateFactory.newLatLng(LatLng(mapList[0].lat, mapList[0].lng)))
//
//            map?.addMarker(
//                MarkerOptions()
//                    .position(LatLng(mapList[0].lat, mapList[0].lng))
//                    .title("시작지점")
//            )
//
//            map?.addMarker(
//                MarkerOptions()
//                    .position(LatLng(mapList[mapList.size - 1].lat, mapList[mapList.size - 1].lng))
//                    .title("종료지점")
//            )
//
//            for (i in mapList) {
//                polylineOptions.add(LatLng(i.lat, i.lng))
//                polylineOptions.width(13f)
//                polylineOptions.visible(true)   // 선이 보여질지/안보여질지 옵션.
//
//                map?.addPolyline(polylineOptions)
//            }
////        }
//    }

    fun initMap(mapList:ArrayList<MapVO>){

        Log.d("test2-mapList", mapList.toString())
        var startLatLng = LatLng(mapList[0].lat, mapList[0].lng)
        Log.d("test3-startLatLng", startLatLng.toString())

        mapCourseMapFull.getMapAsync {

            map = it
            map.getUiSettings().setZoomControlsEnabled(true);

            map!!.moveCamera(CameraUpdateFactory.newLatLng(LatLng(mapList[0].lat, mapList[0].lng)))

            map.animateCamera(CameraUpdateFactory.zoomTo(15f))

            map!!.addMarker(
                MarkerOptions()
                    .position(LatLng(mapList[0].lat, mapList[0].lng))
                    .title("시작지점")
            )

            map!!.addMarker(
                MarkerOptions()
                    .position(LatLng(mapList[mapList.size - 1].lat, mapList[mapList.size - 1].lng))
                    .title("종료지점")
            )

            for (i in mapList) {
                polylineOptions.add(LatLng(i.lat, i.lng))
                polylineOptions.width(13f)
                polylineOptions.visible(true)   // 선이 보여질지/안보여질지 옵션.

                map!!.addPolyline(polylineOptions)
            }

        }

    }

    fun settingMap(mapList: ArrayList<MapVO>) {

        if(mapList.size>=1) {
            map?.moveCamera(CameraUpdateFactory.newLatLng(LatLng(mapList[0].lat, mapList[0].lng)))

            map?.addMarker(
                MarkerOptions()
                    .position(LatLng(mapList[0].lat, mapList[0].lng))
                    .title("시작지점")
            )

            map?.addMarker(
                MarkerOptions()
                    .position(LatLng(mapList[mapList.size - 1].lat, mapList[mapList.size - 1].lng))
                    .title("종료지점")
            )

            for (i in mapList) {
                polylineOptions.add(LatLng(i.lat, i.lng))
                polylineOptions.width(13f)
                polylineOptions.visible(true)   // 선이 보여질지/안보여질지 옵션.

                map?.addPolyline(polylineOptions)
            }
        }
    }

}