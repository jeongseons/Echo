package com.example.echo.group

import android.app.Activity
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

class JoinGroupProfileActivity : Activity() {
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
    lateinit var tvJoinGroupProfileDetail : TextView


    var id: String = ""
    var group_title = ""

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
        tvJoinGroupProfileDetail = findViewById(R.id.tvJoinGroupProfileDetail)
        btnJoinGroupProfileJoin = findViewById<Button>(R.id.btnJoinGroupProfileJoin)

        val imgJoinGroupProfileMoveBack = findViewById<ImageView>(R.id.imgJoinGroupProfileMoveBack)
        imgJoinGroupProfileMoveBack.setOnClickListener {
            finish()
        }

        val seq = intent.getIntExtra("num", 0)

        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(ContentValues.TAG, "????????? ?????? ?????? ??????", error)
            }
            else if (user != null) {
                id = user.id.toString()
                Log.d("?????????-id",id)

                JoinGroupPro(seq, id)
                Log.d("?????????-?????????","????????? ??????")



            }
        }


        //?????? ?????? ????????? ??? ?????? ????????? ?????? ?????? ??????
        btnJoinGroupProfileJoin.setOnClickListener {
            val dialog: AlertDialog.Builder = AlertDialog.Builder(
                this@JoinGroupProfileActivity,
                android.R.style.ThemeOverlay_Material_Dialog_Alert
            )
            dialog.setMessage("${group_title}??? ?????? ????????? ???????????????????")
                .setTitle("?????? ??????")
                .setPositiveButton("?????????", DialogInterface.OnClickListener { dialog, which ->
                    Log.i("Dialog", "??????")
                })
                .setNeutralButton("???",
                    DialogInterface.OnClickListener { dialog, which ->
                        GroupSignUp(seq, id)
                        Toast.makeText(this,"?????? ????????? ?????????????????????",Toast.LENGTH_SHORT).show()
                        finish()
                        Log.d("?????????-???????????????","????????????")
                    })
                .show()
        }


    }//???????????? ??? ???

    fun GroupSignUp(num:Int, id: String) {//?????? ????????????
        val call = RetrofitBuilder.api.groupSignUp(num, id)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {//??????
                    Log.d("??????", response.body().toString())
                    Log.d("?????????-???????????????","????????????2")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("????????? ??????", "?")
                Log.d("?????????-???????????????","????????????4")
            }
        })
    }


    fun JoinGroupPro(num: Int , id: String) {//?????? ???????????? ????????? - ????????? ??????
        val call = RetrofitBuilder.api.joinGroupPro(num, id)
        call.enqueue(object : Callback<JoinGroupVO> {
            override fun onResponse(call: Call<JoinGroupVO>, response: Response<JoinGroupVO>) {
                if (response.isSuccessful) {//??????
                    Log.d("????????????", response.body().toString())
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

                    group_title = groupInfo.group_name
                    tvJoinGroupProfileTitle.setText(groupInfo.group_name)
                    tvJoinGroupProfileMaster.setText(groupInfo.group_owner_id)
                    tvJoinGroupProfileMax.setText("${groupInfo.group_current}/${groupInfo.user_max}")
                    tvJoinGroupProfileType.setText(groupInfo.group_type)
                    tvJoinGroupProfileArea.setText(groupInfo.group_area)
                    tvJoinGroupProfileGender.setText(groupInfo.group_gender)
                    tvJoinGroupProfileAge.setText(groupInfo.group_age)
                    tvJoinGroupProfileLevel.setText(groupInfo.group_level)
                    tvJoinGroupProfileDetail.setText(groupInfo.group_detail)


                    if(groupInfo.group_auth != ("0")){
                        btnJoinGroupProfileJoin.setText("?????? ?????? ?????? ???????????????")
                        btnJoinGroupProfileJoin.isClickable=false
                    }

                    if(groupInfo.group_profile_img.length <5) {
                        imgJoinGroupProfile.setImageResource(R.drawable.p1)
                    }else {
                        Glide.with(this@JoinGroupProfileActivity)
                            .load(groupInfo.group_profile_img)
                            .into(imgJoinGroupProfile)
                    }

                    Log.d("????????????2" ,groupInfo.toString())






                }
            }
            override fun onFailure(call: Call<JoinGroupVO>, t: Throwable) {
                Log.d("????????? ??????", "?")
            }
        })
    }

}