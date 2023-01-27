package com.example.echo.group

import android.content.ContentValues
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.echo.R
import com.example.echo.RetrofitBuilder
import com.kakao.sdk.user.UserApiClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JoinGroupProfileActivity : AppCompatActivity() {
    lateinit var groupInfo : JoinGroupVO
    lateinit var tvJoinGroupProfileTitle : TextView
    lateinit var tvJoinGroupProfileMaster : TextView
    lateinit var tvJoinGroupProfileMax : TextView
    lateinit var tvJoinGroupProfileType : TextView
    lateinit var tvJoinGroupProfileArea : TextView
    lateinit var tvJoinGroupProfileGender : TextView
    lateinit var tvJoinGroupProfileAge : TextView
    lateinit var tvJoinGroupProfileLevel : TextView
    lateinit var imgJoinGroupProfile : ImageView
    lateinit var btnJoinGroupProfileJoin : Button
    lateinit var tvJoinGroupProfileDate : TextView
    lateinit var tvJoinGroupProfileDetail : TextView


    var id: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_group_profile)

        tvJoinGroupProfileTitle = findViewById(R.id.tvJoinGroupProfileTitle)
        tvJoinGroupProfileMaster = findViewById(R.id.tvJoinGroupProfileMaster)
        tvJoinGroupProfileMax = findViewById(R.id.tvJoinGroupProfileMax)
        tvJoinGroupProfileType = findViewById(R.id.tvJoinGroupProfileType)
        tvJoinGroupProfileArea = findViewById(R.id.tvJoinGroupProfileArea)
        tvJoinGroupProfileGender = findViewById(R.id.tvJoinGroupProfileGender)
        tvJoinGroupProfileAge = findViewById(R.id.tvJoinGroupProfileAge)
        tvJoinGroupProfileLevel = findViewById(R.id.tvJoinGroupProfileLevel)
        imgJoinGroupProfile = findViewById(R.id.imgJoinGroupProfile)
        tvJoinGroupProfileDate = findViewById(R.id.tvJoinGroupProfileDate)
        tvJoinGroupProfileDetail = findViewById(R.id.tvJoinGroupProfileDetail)

        btnJoinGroupProfileJoin = findViewById<Button>(R.id.btnJoinGroupProfileJoin)

        val seq = intent.getIntExtra("num", 0)

        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(ContentValues.TAG, "사용자 정보 요청 실패", error)
            }
            else if (user != null) {
                id = user.id.toString()
                Log.d("값확인-id",id)

                JoinGroupPro(seq, id)
                Log.d("값확인-위에서","왔는지 확인")



            }
        }


        //가입 신청 눌렀을 때 해당 모임에 가입 신청 하기
        btnJoinGroupProfileJoin.setOnClickListener {
            val dialog: AlertDialog.Builder = AlertDialog.Builder(
                this@JoinGroupProfileActivity,
                android.R.style.Theme_DeviceDefault_Light_Dialog
            )
            dialog.setMessage("모임이름 에 가입 신청을 하시겠습니까?")
                .setTitle("가입 신청")
                .setPositiveButton("아니오", DialogInterface.OnClickListener { dialog, which ->
                    Log.i("Dialog", "취소")
                })
                .setNeutralButton("예", //다이얼로그 현재 안되는듯합니다.
                    DialogInterface.OnClickListener { dialog, which ->
                        GroupSignUp(seq, id)
                        Toast.makeText(this,"가입 신청이 완료되었습니다",Toast.LENGTH_SHORT).show()
                        finish()
                        Log.d("값확인-다이얼로그","도착확인")
                    })
                .show()
        }


    }//액티비티 뷰 끝

    fun GroupSignUp(num:Int, id: String) {//그룹 가입신청
        val call = RetrofitBuilder.api.groupSignUp(num, id)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {//성공
                    Log.d("성공", response.body().toString())
                    Log.d("값확인-다이얼로그","도착확인2")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("반환값 에러", "?")
                Log.d("값확인-다이얼로그","도착확인4")
            }
        })
    }


    fun JoinGroupPro(num: Int , id: String) {//그룹 조건검색 리스트 - 스프링 통신
        val call = RetrofitBuilder.api.joinGroupPro(num, id)
        call.enqueue(object : Callback<JoinGroupVO> {
            override fun onResponse(call: Call<JoinGroupVO>, response: Response<JoinGroupVO>) {
                if (response.isSuccessful) {//성공
                    Log.d("그룹조회", response.body().toString())
                    groupInfo = JoinGroupVO(response.body()!!.group_seq,
                        response.body()!!.group_profile_img,
                        response.body()!!.group_auth,
                        response.body()!!.group_current,
                        response.body()!!.group_name,
                        response.body()!!.group_owner_id,
                        response.body()!!.group_area,
                        response.body()!!.user_max,
                        response.body()!!.group_dt,
                        response.body()!!.group_age,
                        response.body()!!.group_level,
                        response.body()!!.group_gender,
                        response.body()!!.group_type,
                        response.body()!!.group_detail)

                    tvJoinGroupProfileTitle.setText(groupInfo.group_name)
                    tvJoinGroupProfileMaster.setText(groupInfo.group_owner_id)
                    tvJoinGroupProfileMax.setText("${groupInfo.group_current}/${groupInfo.user_max}")
                    tvJoinGroupProfileType.setText(groupInfo.group_type)
                    tvJoinGroupProfileArea.setText(groupInfo.group_area)
                    tvJoinGroupProfileGender.setText(groupInfo.group_gender)
                    tvJoinGroupProfileAge.setText(groupInfo.group_age)
                    tvJoinGroupProfileLevel.setText(groupInfo.group_level)
                    tvJoinGroupProfileDetail.setText(groupInfo.group_detail)
                    tvJoinGroupProfileDate.setText(groupInfo.group_dt)


                    if(groupInfo.group_auth != ("0")){
                        btnJoinGroupProfileJoin.setText("이미 가입 중인 모임입니다")
                        btnJoinGroupProfileJoin.isClickable=false
                    }

                    if(groupInfo.group_profile_img.length <5) {
                        imgJoinGroupProfile.setImageResource(R.drawable.p1)
                    }else {
                        Glide.with(this@JoinGroupProfileActivity)
                            .load(groupInfo.group_profile_img)
                            .into(imgJoinGroupProfile)
                    }

                    Log.d("그룹조회2" ,groupInfo.toString())






                }
            }
            override fun onFailure(call: Call<JoinGroupVO>, t: Throwable) {
                Log.d("반환값 에러", "?")
            }
        })
    }

}