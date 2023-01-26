package com.example.echo.group

import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import com.bumptech.glide.Glide
import com.example.echo.R
import com.example.echo.RetrofitBuilder
import com.example.echo.myPage.user_id
import com.kakao.sdk.user.UserApiClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JoinGroupProfileActivity : AppCompatActivity() {
    lateinit var groupInfo: JoinGroupVO
    var id: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_group_profile)

        val tvJoinGroupProfileTitle = findViewById<TextView>(R.id.tvJoinGroupProfileTitle)
        val tvJoinGroupProfileMaster = findViewById<TextView>(R.id.tvJoinGroupProfileMaster)
        val tvJoinGroupProfileMax = findViewById<TextView>(R.id.tvJoinGroupProfileMax)
        val tvJoinGroupProfileType = findViewById<TextView>(R.id.tvJoinGroupProfileType)
        val tvJoinGroupProfileArea = findViewById<TextView>(R.id.tvJoinGroupProfileArea)
        val tvJoinGroupProfileGender = findViewById<TextView>(R.id.tvJoinGroupProfileGender)
        val tvJoinGroupProfileAge = findViewById<TextView>(R.id.tvJoinGroupProfileAge)
        val tvJoinGroupProfileLevel = findViewById<TextView>(R.id.tvJoinGroupProfileLevel)

        val btnJoinGroupProfileJoin = findViewById<Button>(R.id.btnJoinGroupProfileJoin)
        val imgJoinGroupProfile = findViewById<ImageView>(R.id.imgJoinGroupProfile)

        val seq = intent.getIntExtra("num", 0)

        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(ContentValues.TAG, "사용자 정보 요청 실패", error)
            }
            else if (user != null) {
                id = user.id.toString()
                Log.d("id",id)

                JoinGroupPro(seq, id)
            }
        }


        tvJoinGroupProfileTitle.setText(groupInfo.group_name)
        tvJoinGroupProfileMaster.setText(groupInfo.group_owner_id)
        tvJoinGroupProfileMax.setText("${groupInfo.group_current}/${groupInfo.user_max}")
        tvJoinGroupProfileType.setText(groupInfo.group_type)
        tvJoinGroupProfileArea.setText(groupInfo.group_area)
        tvJoinGroupProfileGender.setText(groupInfo.group_gender)
        tvJoinGroupProfileAge.setText(groupInfo.group_age)
        tvJoinGroupProfileLevel.setText(groupInfo.group_level)

        if(groupInfo.group_profile_img.length <5) {
            imgJoinGroupProfile.setImageResource(R.drawable.p1)
        }else {
            Glide.with(this)
                .load(groupInfo.group_profile_img)
                .into(imgJoinGroupProfile)
        }


        //groupInfo.group_auth => 0이 아니면 버튼 비활성화 하는 로직 추가해주세요! (디자인이나 버튼위치, toast를 할건지 선택)
        //groupInfo.group_auth가 0이어야 미가입 상태임.




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
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("반환값 에러", "?")
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
                        response.body()!!.group_age,
                        response.body()!!.group_level,
                        response.body()!!.group_gender,
                        response.body()!!.group_type,
                        response.body()!!.group_detail)
                    Log.d("그룹조회2" ,groupInfo.toString())
                }
            }
            override fun onFailure(call: Call<JoinGroupVO>, t: Throwable) {
                Log.d("반환값 에러", "?")
            }
        })
    }

}