package com.example.echo.path


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.echo.MainActivity
import com.example.echo.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.android.synthetic.main.activity_setting.*

private var mMap: GoogleMap? = null
private val polylineOptions = PolylineOptions().width(7f).color(Color.RED)


class MapSaveActivity : AppCompatActivity(), MapFragment4.OnConnectedListener {

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

        var latlngArray = intent.getSerializableExtra("latlngArray") as ArrayList<Pair<Double,Double>>
        val totalTime = intent.getStringExtra("totalTime")
        val totalAlt = intent.getStringExtra("totalAlt")
        val totalDistance = intent.getStringExtra("totalDistance")
        val speed = intent.getStringExtra("user_nick")

        val bundle: Bundle = Bundle()
        bundle.putSerializable("latlngArray", latlngArray)
        fragment.arguments = bundle

        tvMapSaveTime.text = totalTime
        tvMapSaveAlt.text = totalAlt
        tvMapSaveDistance.text = totalDistance


        supportFragmentManager.beginTransaction().replace(
            R.id.flMap2,
            MapFragment4()
        ).commit()


        btnSaveBack.setOnClickListener {

            finish()

        }

        btnMapSave.setOnClickListener {

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


}