package com.example.echo.myPage

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.echo.R
import com.example.echo.path.MapSaveActivity
import com.example.echo.path.MapVO
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


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

        val imgCourseMapFullCapture = findViewById<ImageView>(R.id.imgCourseMapFullCapture)
        imgCourseMapFullCapture.setOnClickListener {
            map.snapshot {
                //SnapshotReadyCallback
                it?.let{
                    saveMediaToStorage(it)
                }
            }
        }

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
        var endLatLng = LatLng(mapList[mapList.size - 1].lat, mapList[mapList.size - 1].lng)
        var centerLatLng = LatLng((startLatLng.latitude+endLatLng.latitude)/2, (startLatLng.longitude+endLatLng.longitude)/2)

        mapCourseMapFull.getMapAsync {

            map = it
            map.getUiSettings().setZoomControlsEnabled(true);

            map.setOnMapLoadedCallback {
                try {
                    map.isMyLocationEnabled = true   //현재위치표시 및 현재위치로 돌아올 수 있는 버튼 생성.
                } catch (e: SecurityException) {
                }
            }

            map!!.moveCamera(CameraUpdateFactory.newLatLng(centerLatLng))
            var mapSaveActivity = MapSaveActivity()
            var zoomDistance = mapSaveActivity.getDistance(startLatLng, endLatLng)
            map!!.moveCamera(CameraUpdateFactory.newLatLng(centerLatLng))
            if (zoomDistance >= 5000) {
                map!!.animateCamera(CameraUpdateFactory.zoomTo(11f))
            } else if (zoomDistance >= 3500) {
                map!!.animateCamera(CameraUpdateFactory.zoomTo(12f))
            } else if (zoomDistance >= 1500) {
                map!!.animateCamera(CameraUpdateFactory.zoomTo(13f))
            } else {
                map!!.animateCamera(CameraUpdateFactory.zoomTo(15f))
            }

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

        fun saveMediaToStorage(bitmap: Bitmap) {
        // Generating a file name
        val filename = "${System.currentTimeMillis()}.jpg"

        // Output stream
        var fos: OutputStream? = null

        // For devices running android >= Q
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // getting the contentResolver
            this.contentResolver?.also { resolver ->

                // Content resolver will process the contentvalues
                val contentValues = ContentValues().apply {

                    // putting file information in content values
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }

                // Inserting the contentValues to
                // contentResolver and getting the Uri
                val imageUri: Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                // Opening an outputstream with the Uri that we got
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            // These for devices running on android < Q
            val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }

        fos?.use {
            // Finally writing the bitmap to the output stream that we opened
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            Toast.makeText(this , "갤러리에 저장되었습니다" , Toast.LENGTH_SHORT).show()
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