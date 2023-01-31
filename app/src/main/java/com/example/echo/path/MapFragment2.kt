package com.example.echo.path

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.location.*
import android.location.LocationListener
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.echo.MainActivity
import com.example.echo.R
import com.example.echo.databinding.ActivityMainBinding
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.android.synthetic.main.fragment_map2.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread


class MapFragment2 : Fragment(), OnMapReadyCallback {
    lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // 2. Context를 Activity로 형변환하여 할당
        mainActivity = context as MainActivity
    }

    val MY_PERMISSION_ACCESS_ALL = 100
    lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var googleMap: GoogleMap? = null
    private lateinit var mView: MapView
    private var currentMarker: Marker? = null
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    lateinit var manager: LocationManager
    lateinit var locationListener: LocationListener
    lateinit var tvMapCurrentTime2: TextView
    lateinit var currentlocation: LatLng
    var total = 0
    var started = false
    var TimeTaken = null
    var total_distance: Double = 0.0
    lateinit var startPoint: LatLng
    lateinit var endPoint: LatLng


    @SuppressLint("MissingPermission", "MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val view = inflater.inflate(R.layout.fragment_map2, container, false)

        currentlocation = LatLng(latitude, longitude)
        val geocoder = Geocoder(this.context)
        val address: List<Address>? = null
        var tvMapTimer2 = view.findViewById<TextView>(R.id.tvMapTimer2)
        val btnMapRecordStart2 = view.findViewById<Button>(R.id.btnMapRecordStart2)
        val btnMapRecordEnd2 = view.findViewById<Button>(R.id.btnMapRecordEnd2)
        tvMapCurrentTime2 = view.findViewById(R.id.tvMapCurrentTime2)
        val tvMApCurrentLocation2 = view.findViewById<TextView>(R.id.tvMApCurrentLocation2)
        val tvMapTakenDistance = view.findViewById<TextView>(R.id.tvMapTakenDistance)
        val btnMapRecordPause = view.findViewById<Button>(R.id.btnMapRecordPause)


        mView = view.findViewById(R.id.map2) as MapView
        mView.onCreate(savedInstanceState)
        mView.getMapAsync(this)



        btnMapRecordEnd2.visibility = View.INVISIBLE
        btnMapRecordPause.visibility = View.INVISIBLE


        btnMapRecordStart2.setOnClickListener {
            Log.d("산행중gps값받아오기","${latitude},${longitude}")
            val polyLineOptions = PolylineOptions().width(5f).color(Color.BLUE)
            /*polyLineOptions.add(myLocation)*/
            btnMapRecordStart2.visibility = View.INVISIBLE
            btnMapRecordEnd2.visibility = View.VISIBLE
            btnMapRecordPause.visibility = View.VISIBLE
            startTimer()
        }

        btnMapRecordPause.setOnClickListener {
            pauseTimer()
        }

        btnMapRecordEnd2.setOnClickListener {
            stop()
            googleMap?.snapshot {
                it?.let{
                    saveMediaToStorage(it)
                }
            }

 //           val intent = Intent(requireContext(), MapSaveActivity::class.java)
//            intent.putExtra("minute", "$hour`")
//            startActivity(intent)
        }

        currentTime()
        tvMApCurrentLocation2.text = getCurrentAddress(currentlocation).toString()

        return view
    }

     fun finish() {
        finish()

    }


    //Media저장 메소드
    private fun saveMediaToStorage(bitmap: Bitmap) {
        // Generating a file name
        val filename = "${System.currentTimeMillis()}.jpg"

        // Output stream
        var fos: OutputStream? = null

        // For devices running android >= Q
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // getting the contentResolver
            requireContext().contentResolver?.also { resolver ->

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
            Toast.makeText(requireContext() , "Captured View and saved to Gallery" , Toast.LENGTH_SHORT).show()
        }
    }


    //StartTimer
    fun startTimer(){
        started = true
        thread(start=true) {
            //thread안의 start : thread를 시작하는 start
            while (true){
                Thread.sleep(1000)
                if(!started) break
                total = total+1
                mainActivity.runOnUiThread {
                    tvMapTimer2.text = formatTime(total)
                }
            }
        }
    }


    //PauseTimer
    fun pauseTimer(){
        started=false
    }

    //EndTimer
    fun stop(){
        started = false
        total = 0
        tvMapTimer2.text = "00:00:00"
    }

    fun formatTime(time:Int) : String {
        val second = String.format("%02d", total%60)
        val minute = String.format("%02d", total/60 % 60)
        val hour = String.format("%02d", total/60/60)
       // TimeTaken = concat
        return "$hour:$minute:$second"
    }

    //실시간 이동거리 구하기
    fun distanceBetween(startPoint : LatLng, endPoint:LatLng) {

    }

    //현재시간 구하기
    fun currentTime() {

        val currentTime = System.currentTimeMillis()
        convertTimestampToDate(currentTime)

    }

    fun convertTimestampToDate(timestamp: Long) {
        val time = SimpleDateFormat("yyyy년 MM월 dd일 hh시 mm분")
        val date = time.format(timestamp)
        tvMapCurrentTime2.text = date
        //2021년06월10일02시37분
    }

    override fun onMapReady(googleMap: GoogleMap) {

        Log.d("도착했니", "퍼미션허가")
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireContext() as Activity,
                arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION),
                0
            )

        } else {
            fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(requireContext())
            val fusedLocationClient: FusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(requireContext())

            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    latitude = location.latitude
                    longitude = location.longitude
                    currentlocation = LatLng(latitude, longitude)
                    Log.d(
                        "여기는 onMapReady Test",
                        "GPS Location Latitude: $latitude" + ", Longitude: $longitude"
                    )

                    googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentlocation, 19F))

                    googleMap?.moveCamera(CameraUpdateFactory.newLatLng(currentlocation))
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(currentlocation)
                            .title("현재 위치")
                    )
                    getCurrentAddress(currentlocation)
                    googleMap.isMyLocationEnabled = true
                    googleMap.uiSettings.apply {
                        isCompassEnabled = true
                    }

                                    btnMapRecordEnd2.setOnClickListener {
                                        stop()
                                        googleMap?.snapshot {
                                            it?.let {
                                                saveMediaToStorage(it)
                                            }
                                        }
                                    }
                                } else {
                                    Log.d("null Test2", "null값")
                                }
                            }
                        }
                    }




    fun getCurrentAddress(currentlocation : LatLng): String? {

        //지오코더... GPS를 주소로 변환
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses: List<Address>?
        addresses = try {
            geocoder.getFromLocation(
                currentlocation.latitude,
                currentlocation.longitude,
                1
            )
        } catch (ioException: IOException) {
            //네트워크 문제
            Toast.makeText(requireContext(), "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show()
            return "지오코더 서비스 사용불가"
        } catch (illegalArgumentException: IllegalArgumentException) {
            Toast.makeText(requireContext(), "잘못된 GPS 좌표", Toast.LENGTH_LONG).show()
            return "잘못된 GPS 좌표"
        }
        return if (addresses == null || addresses.size == 0) {
            Toast.makeText(requireContext(), "주소 미발견", Toast.LENGTH_LONG).show()
            "주소 미발견"
        } else {
            val address = addresses[0]
            address.getAddressLine(0).toString()
        }

    }

    override fun onStart() {
        super.onStart()
        mView.onStart()


    }
    override fun onStop() {
        super.onStop()
        mView.onStop()
    }
    override fun onResume() {
        super.onResume()
        mView.onResume()
    }
    override fun onPause() {
        super.onPause()
        mView.onPause()
    }
    override fun onLowMemory() {
        super.onLowMemory()
        mView.onLowMemory()
    }
    override fun onDestroy() {
        mView.onDestroy()
        super.onDestroy()
    }




}

typealias Polylines = MutableList<Polyline>

typealias Polyline = MutableList<LatLng>