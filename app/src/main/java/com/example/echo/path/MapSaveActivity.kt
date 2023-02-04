package com.example.echo.path


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.echo.MainActivity
import com.example.echo.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.android.synthetic.main.activity_map_save.*
import kotlinx.android.synthetic.main.activity_setting.*

private var mMap: GoogleMap? = null
private val polylineOptions = PolylineOptions().width(7f).color(Color.RED)
var course_type = ""
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
        val totalTime = intent.getStringExtra("totalTime")
        val totalAlt = intent.getStringExtra("totalAlt")
        val totalDistance = intent.getStringExtra("totalDistance")
        val speed = intent.getStringExtra("user_nick")

//        making_map(latlngArray!!)
        settingMap(latlngArray!!)

        val mapFragment4 : MapFragment4 = MapFragment4()
        val bundle: Bundle = Bundle()
        bundle.putSerializable("latlngArray", latlngArray)
        mapFragment4.arguments = bundle


        tvMapSaveTime.text = totalTime
        tvMapSaveAlt.text = totalAlt
        tvMapSaveDistance.text = totalDistance


        supportFragmentManager.beginTransaction().replace(
            R.id.flMap2,
            MapFragment4()
        ).commit()

        rdoMapSaveType.setOnCheckedChangeListener { _, checkedId ->
            Log.d(" ", "RadioButton is Clicked")
            when (checkedId) {
                R.id.rdoMapSavePublic-> {
                    course_type = "공개"
                }
                R.id.rdoMapSaveClosed -> {
                    course_type = "비공개"
                }
            }
            Log.d("외않되", course_type)

        }


        btnSaveBack.setOnClickListener {

            finish()

        }

        btnMapSave.setOnClickListener {

            val courseTitle = etMapSaveTitle.text


        val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        Log.d("test", latlngArray.toString() )

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

    override fun onConnect(map: GoogleMap) {
        mMap = map
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

    fun settingMap(latlngArray: ArrayList<Pair<Double,Double>>){

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

    fun uploadCourse(){

    }

}