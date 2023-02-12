package com.example.echo.myPage

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
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
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.share.WebSharerClient
import com.kakao.sdk.template.model.Button
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.Link
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private var mMap: GoogleMap? = null
private var polylineOptions = PolylineOptions().width(7f).color(Color.RED)
var course_seq = 0

class CourseDetailActivity : AppCompatActivity()  {
//    class CourseDetailActivity : AppCompatActivity(), MapFragment4.OnConnectedListener  {
    private lateinit var binding: ActivityCourseDetailBinding
    var mapList = ArrayList<MapVO>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCourseDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mvCourseDetail.onCreate(savedInstanceState)
        binding.mvCourseDetail.onResume()


        course_seq = intent.getIntExtra("course_seq",0)
        Log.d("test-맵", course_seq.toString())
        getMap(course_seq)

        var course_title = intent.getStringExtra("course_title")
        var course_time = intent.getStringExtra("course_time")
        var course_alt = intent.getStringExtra("course_alt")
        var course_distance = intent.getStringExtra("course_distance")
        var course_start_dt = intent.getStringExtra("course_start_dt")
        var course_end_dt = intent.getStringExtra("course_end_dt")
        var course_open = intent.getStringExtra("course_open")
        var course_user_id = intent.getStringExtra("course_user_id")
        var course_img = intent.getStringExtra("course_img")

        binding.tvCourseDetailTitle.text = course_title
        binding.tvCourseDetailStartTime.text = course_start_dt
        binding.tvCourseDetailEndTime.text = course_end_dt
        binding.tvCourseDetailTotalTime.text = course_time
        binding.tvCourseDetailTotalDistance.text = course_distance
        binding.tvCourseDetailTotalAlt.text = course_alt

//        supportFragmentManager.beginTransaction().replace(
//            R.id.flCourseDetail,
//            MapFragment4()
//        ).commit()


        binding.imgCourseDetailCloseup.setOnClickListener{
            val intent = Intent(this, CourseMapActivity::class.java)
            intent.putExtra("mapList", mapList)
            startActivity(intent)
        }

        //카카오톡 공유
        binding.imgCourseDetailShare.setOnClickListener{
            val defaultFeed = FeedTemplate(
                content = Content(
                    title = course_title!!,
                    imageUrl = course_img!!,
                    link = Link(
                        webUrl = "https://developers.kakao.com",
                        mobileWebUrl = "https://developers.kakao.com"
                    )
                ),
                buttons = listOf(
                    Button(
                        "웹으로 보기",
                        Link(
                            webUrl = "https://developers.kakao.com",
                            mobileWebUrl = "https://developers.kakao.com"
                        )
                    ),
                    Button(
                        "앱으로 보기",
                        Link(
                            androidExecutionParams = mapOf("key1" to "value1", "key2" to "value2"),
                            iosExecutionParams = mapOf("key1" to "value1", "key2" to "value2")
                        )
                    )
                )
            )

            if (ShareClient.instance.isKakaoTalkSharingAvailable(this)) {
                // 카카오톡으로 카카오톡 공유 가능
                ShareClient.instance.shareDefault(this, defaultFeed) { sharingResult, error ->
                    if (error != null) {
                        Log.d("test-카카오톡공유", "카카오톡 공유 실패", error)
                    }
                    else if (sharingResult != null) {
                        Log.d("test-카카오톡공유", "카카오톡 공유 성공 ${sharingResult.intent}")
                        startActivity(sharingResult.intent)
                        // 카카오톡 공유에 성공했지만 아래 경고 메시지가 존재할 경우 일부 컨텐츠가 정상 동작하지 않을 수 있습니다.
                        Log.d("test-카카오톡공유", "Warning Msg: ${sharingResult.warningMsg}")
                        Log.d("test-카카오톡공유", "Argument Msg: ${sharingResult.argumentMsg}")
                    }
                }
            } else {
                // 카카오톡 미설치
                val sharerUrl = WebSharerClient.instance.makeDefaultUrl(defaultFeed)
                try {
                    KakaoCustomTabsClient.openWithDefault(this, sharerUrl)
                } catch(e: UnsupportedOperationException) {
                }

                try {
                    KakaoCustomTabsClient.open(this, sharerUrl)
                } catch (e: ActivityNotFoundException) {

                }
            }
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
                initMap(mapList)
                Log.d("test-getMap",mapList.toString())
            }
            override fun onFailure(call: Call<List<MapVO>>, t: Throwable) {
                Log.d("test-getMap", t.localizedMessage)

            }
        })
    }

    fun initMap(mapList:ArrayList<MapVO>){

        Log.d("test2-mapList", mapList.toString())
        var startLatLng = LatLng(mapList[0].lat, mapList[0].lng)
        Log.d("test3-startLatLng", startLatLng.toString())
        var endLatLng = LatLng(mapList[mapList.size - 1].lat, mapList[mapList.size - 1].lng)
        var centerLatLng = LatLng((startLatLng.latitude+endLatLng.latitude)/2, (startLatLng.longitude+endLatLng.longitude)/2)

        binding.mvCourseDetail.getMapAsync {

            mMap = it
            mMap!!.getUiSettings().setZoomControlsEnabled(true);

            mMap!!.setOnMapLoadedCallback {
                try {
                    mMap!!.isMyLocationEnabled = true   //현재위치표시 및 현재위치로 돌아올 수 있는 버튼 생성.
                } catch (e: SecurityException) {
                }
            }

            var mapSaveActivity = MapSaveActivity()
            var zoomDistance = mapSaveActivity.getDistance(startLatLng, endLatLng)
            mMap!!.moveCamera(CameraUpdateFactory.newLatLng(centerLatLng))
            if (zoomDistance >= 5000) {
                mMap!!.animateCamera(CameraUpdateFactory.zoomTo(11f))
            } else if (zoomDistance >= 3500) {
                mMap!!.animateCamera(CameraUpdateFactory.zoomTo(13f))
            } else if (zoomDistance >= 1500) {
                mMap!!.animateCamera(CameraUpdateFactory.zoomTo(13f))
            } else {
                mMap!!.animateCamera(CameraUpdateFactory.zoomTo(14f))
            }

            mMap!!.addMarker(
                MarkerOptions()
                    .position(LatLng(mapList[0].lat, mapList[0].lng))
                    .title("시작지점")
            )

            mMap!!.addMarker(
                MarkerOptions()
                    .position(LatLng(mapList[mapList.size - 1].lat, mapList[mapList.size - 1].lng))
                    .title("종료지점")
            )

            for (i in mapList) {
                polylineOptions.add(LatLng(i.lat, i.lng))
                polylineOptions.width(13f)
                polylineOptions.visible(true)   // 선이 보여질지/안보여질지 옵션.

                mMap!!.addPolyline(polylineOptions)
            }

        }

        val geocoder = Geocoder(this)
        var addr:String=""
        var addr2:String=""
        var addr3:String=""

        addr = geocoder.getFromLocation(startLatLng.latitude, startLatLng.longitude, 1).first().adminArea

        if(geocoder.getFromLocation(startLatLng.latitude, startLatLng.longitude, 1).first().subLocality==null) {
            addr3 = geocoder.getFromLocation(startLatLng.latitude,startLatLng.longitude, 1)
                .first().locality
            Log.d("test-맵",addr3.toString())
            binding.tvCourseDetailStartAddress.text = "${addr} ${addr3}"

        }
        else{
            addr2 = geocoder.getFromLocation(startLatLng.latitude,startLatLng.longitude, 1).first().subLocality
            binding.tvCourseDetailStartAddress.text = "${addr} ${addr2}"
            Log.d("test-맵",addr2.toString())
        }

    }




//    override fun onConnect(mMap: GoogleMap) {
//        Log.d("test-맵",mapList.toString())
//
//        if(mapList.isNotEmpty()){
//        var startLatLng = LatLng(mapList[0].lat, mapList[0].lng)
//        var endLatLng = LatLng(mapList[mapList.size-1].lat, mapList[mapList.size-1].lng)
//        var mapSaveActivity = MapSaveActivity()
//        var centerLatLng = LatLng((startLatLng.latitude+endLatLng.latitude)/2, (startLatLng.longitude+endLatLng.longitude)/2)
//        var zoomDistance = mapSaveActivity.getDistance(startLatLng, endLatLng)
//            Log.d("test-맵",zoomDistance.toString())
//            Log.d("test-맵시작",startLatLng.toString())
//            Log.d("test-맵종료",endLatLng.toString())
//
//
//            mMap!!.moveCamera(CameraUpdateFactory.newLatLng(centerLatLng))
//        if(zoomDistance>=5000) {
//            mMap!!.animateCamera(CameraUpdateFactory.zoomTo(11f))
//        }else if(zoomDistance>=3500){
//            mMap!!.animateCamera(CameraUpdateFactory.zoomTo(12f))
//        }else if(zoomDistance>=1500){
//            mMap!!.animateCamera(CameraUpdateFactory.zoomTo(13f))
//        }else{
//            mMap!!.animateCamera(CameraUpdateFactory.zoomTo(15f))
//        }
//            mMap?.addMarker(
//            MarkerOptions()
//                .position(startLatLng)
//                .title("시작지점"))
//
//     mMap?.addMarker(
//            MarkerOptions()
//                .position(endLatLng)
//                .title("종료지점"))
//
//        for(i in mapList) {
//            polylineOptions.add(LatLng(i.lat, i.lng))
//            polylineOptions.width(13f)
//            polylineOptions.visible(true)   // 선이 보여질지/안보여질지 옵션.
//            mMap?.addPolyline(polylineOptions)
//        }
//
//            val geocoder = Geocoder(this)
//            var addr:String=""
//            var addr2:String=""
//            var addr3:String=""
//
//            addr = geocoder.getFromLocation(startLatLng.latitude, startLatLng.longitude, 1).first().adminArea
//
//            if(geocoder.getFromLocation(startLatLng.latitude, startLatLng.longitude, 1).first().subLocality==null) {
//                addr3 = geocoder.getFromLocation(startLatLng.latitude, startLatLng.longitude, 1)
//                    .first().locality
//                Log.d("test-맵",addr3.toString())
//                binding.tvCourseDetailStartAddress.text = "${addr} ${addr3}"
//
//            }
//            else{
//                addr2 = geocoder.getFromLocation(startLatLng.latitude, startLatLng.longitude, 1).first().subLocality
//                binding.tvCourseDetailStartAddress.text = "${addr} ${addr2}"
//                Log.d("test-맵",addr2.toString())
//            }
//
//        }
//    }
//
//
//    override fun onBackPressed() {
//        finish()
//    }

}