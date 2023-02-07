package com.example.echo.myPage

import android.content.Intent
import android.graphics.Color
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.echo.R
import com.example.echo.RetrofitBuilder
import com.example.echo.databinding.ActivityCourseDetailBinding
import com.example.echo.path.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private var mMap: GoogleMap? = null
private var polylineOptions = PolylineOptions().width(7f).color(Color.RED)
var course_seq = 0

class CourseDetailActivity : AppCompatActivity(), MapFragment4.OnConnectedListener  {

    private lateinit var binding: ActivityCourseDetailBinding
    var mapList = ArrayList<MapVO>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCourseDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setContentView(R.layout.activity_course_detail)

//        binding.mapView.onCreate(savedInstanceState)

        course_seq = intent.getIntExtra("course_seq",0)
        Log.d("test-맵", course_seq.toString())
        getMap(course_seq)
        Log.d("test-맵2",getMap(course_seq).toString())


        var course_title = intent.getStringExtra("course_title")
        var course_time = intent.getStringExtra("course_time")
        var course_alt = intent.getStringExtra("course_alt")
        var course_distance = intent.getStringExtra("course_distance")
        var course_start_dt = intent.getStringExtra("course_start_dt")
        var course_end_dt = intent.getStringExtra("course_end_dt")
        var course_open = intent.getStringExtra("course_open")
        var course_user_id = intent.getStringExtra("course_user_id")

        binding.tvCourseDetailTitle.text = course_title

        val geocoder = Geocoder(this)
        try {
            var addr = geocoder.getFromLocation(startLatLng.latitude, startLatLng.longitude, 1).first().adminArea
            binding.tvCourseDetailStartAddress.text = addr
        } catch (e: Exception) {
            e.printStackTrace()
        }

        supportFragmentManager.beginTransaction().replace(
            R.id.flCourseDetail,
            MapFragment4()
        ).commit()


        binding.imgCourseDetailCloseup.setOnClickListener{
                val intent = Intent(this, CourseMapActivity::class.java)
                startActivity(intent)
            }


    }

    fun getMap(course_seq: Int) {
        mapList.clear()
        val call = RetrofitBuilder.courseApi.getMap(course_seq)
        call.enqueue(object : Callback<List<MapVO>> {
            override fun onResponse(call: Call<List<MapVO>>, response: Response<List<MapVO>>) {
                if(response.isSuccessful&& response.body()?.size!!>0){
                    for(i in 0 until response.body()!!.size){
                        mapList.add(response.body()!!.get(i))
                    }
                }
                Log.d("test-전부조회",mapList.toString())
            }
            override fun onFailure(call: Call<List<MapVO>>, t: Throwable) {
                Log.d("test-전부조회", t.localizedMessage)

            }
        })
    }

    override fun onConnect(mMap: GoogleMap) {
        Log.d("test-맵",mapList.toString())

        if(mapList.isNotEmpty()){
        var startLatLng = LatLng(mapList[0].lat, mapList[0].lng)
        var endLatLng = LatLng(mapList[mapList.size-1].lat, mapList[mapList.size-1].lng)
        var mapSaveActivity = MapSaveActivity()
        var centerLatLng = LatLng((startLatLng.latitude+endLatLng.latitude)/2, (startLatLng.longitude+endLatLng.longitude)/2)
        var zoomDistance = mapSaveActivity.getDistance(startLatLng, endLatLng)
            Log.d("test-맵",zoomDistance.toString())
            Log.d("test-맵시작",startLatLng.toString())
            Log.d("test-맵종료",endLatLng.toString())


            mMap!!.moveCamera(CameraUpdateFactory.newLatLng(centerLatLng))
        if(zoomDistance>=5000) {
            mMap!!.animateCamera(CameraUpdateFactory.zoomTo(11f))
        }else if(zoomDistance>=3500){
            mMap!!.animateCamera(CameraUpdateFactory.zoomTo(12f))
        }else if(zoomDistance>=1500){
            mMap!!.animateCamera(CameraUpdateFactory.zoomTo(13f))
        }else{
            mMap!!.animateCamera(CameraUpdateFactory.zoomTo(15f))
        }
            mMap?.addMarker(
            MarkerOptions()
                .position(startLatLng)
                .title("시작지점"))

     mMap?.addMarker(
            MarkerOptions()
                .position(endLatLng)
                .title("종료지점"))

        for(i in mapList) {
            polylineOptions.add(LatLng(i.lat, i.lng))
            polylineOptions.width(13f)
            polylineOptions.visible(true)   // 선이 보여질지/안보여질지 옵션.
            mMap?.addPolyline(polylineOptions)
        }
        }
    }

}