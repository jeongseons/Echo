package com.example.echo.myPage

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.echo.R
import com.example.echo.RetrofitBuilder
import com.example.echo.databinding.ActivityCourseDetailBinding
import com.example.echo.path.MapFragment4
import com.example.echo.path.MapSaveActivity
import com.example.echo.path.MapVO
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
var mapList = ArrayList<MapVO>()

class CourseDetailActivity : AppCompatActivity(), MapFragment4.OnConnectedListener  {

    private lateinit var binding: ActivityCourseDetailBinding

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


        supportFragmentManager.beginTransaction().replace(
            R.id.flCourseDetail,
            MapFragment4()
        ).commit()

    }

    fun getMap(course_seq:Int): ArrayList<MapVO>{
        mapList.clear()
        val call = RetrofitBuilder.courseApi.getMap(course_seq)
        call.enqueue(object : Callback<List<MapVO>> {
            override fun onResponse(call: Call<List<MapVO>>, response: Response<List<MapVO>>) {
                if(response.isSuccessful&& response.body()?.size!!>0){
                    for(i in 0 until response.body()!!.size){
                        mapList.add(response.body()!!.get(i))
                    }
                }
                mapList.reverse()
                Log.d("test-전부조회",mapList.toString())
            }
            override fun onFailure(call: Call<List<MapVO>>, t: Throwable) {
                Log.d("test-전부조회", t.localizedMessage)

            }
        })
        return mapList
    }

    override fun onConnect(mMap: GoogleMap) {
        var mapList = getMap(course_seq)
        Log.d("test-맵",mapList.toString())

        var startLatLng = LatLng(mapList[0].lat, mapList[0].lng)
        var endLatLng = LatLng(mapList[mapList.size-1].lat, mapList[mapList.size-1].lng)
        var mapSaveActivity = MapSaveActivity()
        var zoomDistance = mapSaveActivity.getDistance(startLatLng, endLatLng)

    mMap!!.moveCamera(CameraUpdateFactory.newLatLng(LatLng(mapList[mapList.size/2].lat, mapList[mapList.size/2].lng)))
        if(zoomDistance>5000) {
            mMap!!.animateCamera(CameraUpdateFactory.zoomTo(11f))
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