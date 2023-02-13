package com.example.echo.path


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.echo.MainActivity
import com.example.echo.R
import com.example.echo.RetrofitBuilder
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.kakao.sdk.user.UserApiClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

private var mMap: GoogleMap? = null
private val polylineOptions = PolylineOptions().width(7f).color(Color.RED)
var course_open = "y"
var user_id = ""
var course_title = ""
var course_img = ""
var googleMapUrI = "https://maps.googleapis.com/maps/api/staticmap?size=1080x400&key=AIzaSyATOttvU2LAnTBThyMWVt-hl9faraTD-bY&path=color:0xff0000ff|weight:3"

class MapSaveActivity : AppCompatActivity(), MapFragment4.OnConnectedListener {

    lateinit var imgMapSave:ImageView

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_save)


        val flMap2 = findViewById<FrameLayout>(R.id.flMap2)
        val btnMapSave = findViewById<Button>(R.id.btnMapSave)
        val btnSaveBack = findViewById<ImageView>(R.id.btnSaveBack)
        val tvMapSaveAlt = findViewById<TextView>(R.id.tvMapSaveAlt)
        val tvMapSaveDistance = findViewById<TextView>(R.id.tvMapSaveDistance)
        val tvMapSaveTime = findViewById<TextView>(R.id.tvMapSaveTime)
        val etMapSaveTitle = findViewById<EditText>(R.id.etMapSaveTitle)
        var rdoMapSaveType = findViewById<RadioGroup>(R.id.rdoMapSaveType)

        imgMapSave = findViewById(R.id.imgMapSave)

        rdoMapSaveType.check(R.id.rdoMapSavePublic)

        var latlngArray = intent.getSerializableExtra("latlngArray") as ArrayList<Pair<Double,Double>>
        val course_time = intent.getStringExtra("totalTime")
        val course_alt = intent.getStringExtra("totalAlt")
        val course_distance = intent.getStringExtra("totalDistance")
        val course_start_dt = intent.getStringExtra("startTime")
        val course_end_dt = intent.getStringExtra("endTime")
        val speed = intent.getStringExtra("user_nick")

//        making_map(latlngArray!!)
//        settingMap(latlngArray!!)

        val mapFragment4 : MapFragment4 = MapFragment4()
        val bundle: Bundle = Bundle()
        bundle.putSerializable("latlngArray", latlngArray)
        mapFragment4.arguments = bundle


        tvMapSaveTime.text = course_time
        tvMapSaveAlt.text = course_alt
        tvMapSaveDistance.text = course_distance

        Log.d("test-시간", course_start_dt.toString())


        supportFragmentManager.beginTransaction().replace(
            R.id.flMap2,
            MapFragment4()
        ).commit()

        rdoMapSaveType.setOnCheckedChangeListener { _, checkedId ->
            Log.d(" ", "RadioButton is Clicked")
            when (checkedId) {
                R.id.rdoMapSavePublic-> {
                    course_open = "y"
                }
                R.id.rdoMapSaveClosed -> {
                    course_open = "n"
                }
            }
            Log.d("test-경로공개여부", course_open.toString())

        }


        btnSaveBack.setOnClickListener {

            finish()

        }

        Log.d("test-courseVO", "${course_time}")
        Log.d("test-courseVO", "${course_alt}")
        Log.d("test-courseVO", "${course_distance}")
        Log.d("test-courseVO", "${course_start_dt}")
        Log.d("test-courseVO", "${course_end_dt}")
        Log.d("test-courseVO", "${course_open}")
        Log.d("test-courseVO", "${latlngArray}")


        btnMapSave.setOnClickListener {

            course_title = etMapSaveTitle.text.toString()

            mMap?.snapshot {
                //SnapshotReadyCallback
                it?.let{
                    imgUpload(it)
                }
            }

            UserApiClient.instance.me { user, error ->
                user_id = user?.id.toString()
                var course = CourseVO(0, user_id, course_title, course_time!!, course_alt!!,
                    course_distance!!, course_start_dt!!, course_end_dt!!, course_open, course_img, latlngArray)
                Log.d("test-courseVO", course.toString())
                addCourse(course)

            }

        }

        Log.d("test", latlngArray.toString() )

    }

    override fun onConnect(map: GoogleMap) {

        var startLatLng = LatLng(latlngArray[0].first,latlngArray[0].second)
        var endLatLng = LatLng(latlngArray[latlngArray.size-1].first,latlngArray[latlngArray.size-1].second)
        var centerLatLng = LatLng((startLatLng.latitude+endLatLng.latitude)/2, (startLatLng.longitude+endLatLng.longitude)/2)

        var zoomDistance = getDistance(startLatLng, endLatLng)
        mMap = map
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(centerLatLng))
        Log.d("test", zoomDistance.toString() )

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
                .position(LatLng(latlngArray[0].first,latlngArray[0].second))
                .title("시작지점"))

        mMap?.addMarker(
            MarkerOptions()
                .position(LatLng(latlngArray[latlngArray.size-1].first,latlngArray[latlngArray.size-1].second))
                .title("종료지점"))

        for(i in latlngArray) {
            polylineOptions.add(LatLng(i.first,i.second))
            polylineOptions.width(13f)
            polylineOptions.visible(true)   // 선이 보여질지/안보여질지 옵션.

            mMap?.addPolyline(polylineOptions)
        }
    }

    // Google Static Map 만들기
    fun making_map(latlngArray: ArrayList<Pair<Double,Double>>){

        // 위도, 경도 표시
        var latlngString: String? = null

        for(latlng in latlngArray ){
            if(latlngString == null)
                latlngString = "|" + latlng.first + "," + latlng.second
            else
                latlngString = latlngString + "|" + latlng.first + "," + latlng.second
        }

        // 시작, 도착 지점 Marker
        val startPoint = "&markers=icon:http://bitly.kr/vTGodx|" +
                latlngArray[0].first + "," + latlngArray[0].second
        val endPoint = "&markers=icon:http://bitly.kr/xVQKMY|" +
                latlngArray[latlngArray.size-1].first + "," + latlngArray[latlngArray.size-1].second

        googleMapUrI = googleMapUrI + latlngString + startPoint + endPoint

        // 업로드 전 전체 경로 미리보기
        Glide.with(this).load(googleMapUrI).into(imgMapSave)
    }

//    fun settingMap(latlngArray: ArrayList<Pair<Double,Double>>){
//
//        mMap?.addMarker(
//            MarkerOptions()
//                .position(LatLng(latlngArray[0].first,latlngArray[0].second))
//                .title("시작지점"))
//
//        mMap?.addMarker(
//            MarkerOptions()
//                .position(LatLng(latlngArray[latlngArray.size-1].first,latlngArray[latlngArray.size-1].second))
//                .title("종료지점"))
//
//        for(i in latlngArray) {
//            polylineOptions.add(LatLng(i.first,i.second))
//            polylineOptions.width(13f)
//            polylineOptions.visible(true)   // 선이 보여질지/안보여질지 옵션.
//
//            mMap?.addPolyline(polylineOptions)
//        }
//    }

    fun addCourse(course: CourseVO){
        val call = RetrofitBuilder.courseApi.addCourse(course)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>
            ) {
                    Toast.makeText(
                        this@MapSaveActivity, "저장되었습니다",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this@MapSaveActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(
                    this@MapSaveActivity, "저장에 실패했습니다. 다시 시도해주세요.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    // 프로필 이미지 업로드
    fun imgUpload(it: Bitmap) {
        var key = "${course_title}${System.currentTimeMillis()}"
        val storage = Firebase.storage
        val storageRef = storage.reference
        val mountainsRef = storageRef.child("${key}.png")
        course_img = "https://firebasestorage.googleapis.com/v0/b/echo-73cf6.appspot.com/o/${key}.png?alt=media"

        val baos = ByteArrayOutputStream()
        it.compress(Bitmap.CompressFormat.JPEG, 50, baos)
        val data = baos.toByteArray()

        var uploadTask = mountainsRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }
    }

    // 거리 계산
    fun getDistance(startLatLng:LatLng, endLatLng: LatLng): Double {
        if (startLatLng ==  endLatLng)
            return 0.0

        var radLat1 = Math.PI * startLatLng.latitude / 180;
        var radLat2 = Math.PI * endLatLng.latitude / 180;
        var theta = startLatLng.longitude - endLatLng.longitude;
        var radTheta = Math.PI * theta / 180;
        var dist = Math.sin(radLat1) * Math.sin(radLat2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.cos(radTheta);
        if (dist > 1)
            dist = 1.0

        dist = Math.acos(dist);
        dist = dist * 180 / Math.PI;
        dist = dist * 60 * 1.1515 * 1.609344 * 1000;
        if (dist < 100) dist = (Math.round(dist / 10) * 10).toDouble()
        else dist = (Math.round(dist / 100) * 100).toDouble()
        // 1000 -> 1km
        return dist
    }

}