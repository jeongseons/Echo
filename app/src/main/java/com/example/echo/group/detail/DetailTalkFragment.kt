package com.example.echo.group.detail

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.ContentValues
import android.content.Intent.getIntent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.findFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.echo.R
import com.example.echo.RetrofitBuilder
import com.example.echo.WebSocketListener
import com.example.echo.group.GroupActivity
import com.example.echo.group.GroupActivity.Companion.testSocket
import com.example.echo.group.GroupListAdapter
import com.example.echo.group.GroupVO
import com.example.echo.group.Message
import com.example.echo.myPage.MyPageVO

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.kakao.sdk.user.UserApiClient

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class DetailTalkFragment : Fragment() {



    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var userLat = 0.0
    var userLng = 0.0
    lateinit var nick:String
    lateinit var id:String

    companion object{
        lateinit var adapter: TalkAdapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail_talk, container, false)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        receiveLocation {  }

        val btnTalkchat1 = view.findViewById<Button>(R.id.btnTalkchat1)
        val btnTalkchat2 = view.findViewById<Button>(R.id.btnTalkchat2)
        val btnTalkchat3 = view.findViewById<Button>(R.id.btnTalkchat3)
        val rvtalk = view.findViewById<RecyclerView>(R.id.rvtalk)
        val imgChatBox = view.findViewById<ImageView>(R.id.imgChatBox)


        imgChatBox.setOnClickListener {
            if(imgChatBox.tag==false){
                btnTalkchat1.visibility=View.GONE
                btnTalkchat2.visibility=View.GONE
                btnTalkchat3.visibility=View.GONE
                imgChatBox.tag = true
            }else{
                btnTalkchat1.visibility=View.VISIBLE
                btnTalkchat2.visibility=View.VISIBLE
                btnTalkchat3.visibility=View.VISIBLE
                imgChatBox.tag = false
            }
        }

        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(ContentValues.TAG, "????????? ?????? ?????? ??????", error)
            }
            else if (user != null) {
                id = user.id.toString()
                getMyPage(id)
            }
        }

        //???????????? ??????(???????????? ?????????)
        val seq = requireActivity().intent.getIntExtra("num", 0)

        adapter = TalkAdapter(requireContext(), WebSocketListener.talkList)
        //????????? ???????????? ??????????????? ?????? ??????????????? ???????????????.
        rvtalk.adapter = adapter
        rvtalk.layoutManager = LinearLayoutManager(requireContext())

        btnTalkchat1.setOnClickListener {//??????????????????
            receiveLocation {  }
            testSocket.send("{\"msg\":\"??????????????????!\", \"sender\":\"${nick}\", " +
                    "\"Lat\":\"${userLat}\", \"Lng\":\"${userLng}\", \"proId\":\"${id}\"}")
        }
        btnTalkchat2.setOnClickListener {//??????????????????
            receiveLocation {  }
            testSocket.send("{\"msg\":\"??????????????????!\", \"sender\":\"${nick}\", " +
                    "\"Lat\":\"${userLat}\", \"Lng\":\"${userLng}\", \"proId\":\"${id}\"}")
        }
        btnTalkchat3.setOnClickListener {//???????????????
            receiveLocation {  }
            testSocket.send("{\"msg\":\"???????????????!\", \"sender\":\"${nick}\", " +
                    "\"Lat\":\"${userLat}\", \"Lng\":\"${userLng}\", \"proId\":\"${id}\"}")
        }

//        //?????? ?????????
//        talkList.add(
//            Message(
//                "good",
//                "2617009803", "04:00"
//            )
//        )

        return view

    }

    fun receiveLocation(block: (location: Location) -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location ->
                block(location)
                Log.d("test-location", location.toString())
                userLat = location.latitude
                userLng = location.longitude
            }
    }

    fun getMyPage(user_id: String) {
        val call = RetrofitBuilder.userApi.getMyPage(user_id)
        call.enqueue(object : Callback<MyPageVO> {
            override fun onResponse(
                call: Call<MyPageVO>, response: Response<MyPageVO>,
            ) {
                if (response.isSuccessful) {
                    Log.d("text-???????????????", "?????????")
                    var body = response.body()!!
                    Log.d("text-???????????????", body.toString())
                    nick = body.user_nick
                }
            }

            override fun onFailure(call: Call<MyPageVO>, t: Throwable) {
            }
        })
    }

}