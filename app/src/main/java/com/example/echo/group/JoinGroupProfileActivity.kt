package com.example.echo.group

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import com.bumptech.glide.Glide
import com.example.echo.R
import com.example.echo.myPage.user_id

class JoinGroupProfileActivity : AppCompatActivity() {
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



        //그룹 seq 가 일치하는 그룹 정보 JoinGroupVO로 가져오기
        val groupInfo = JoinGroupVO(1,
            "1",
            "n",
            1,
            "테스트 가데이터",
            "테스트",
            "광주",
            5,
            "20대이상",
            "하",
            "무관",
            "등산/등반")



        //가져온 정보 띄워주기
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
                .setNeutralButton("예",
                    DialogInterface.OnClickListener { dialog, which ->

                    })
                .show()
        }







    }
}