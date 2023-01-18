package com.example.echo.group

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import androidx.recyclerview.widget.RecyclerView
import com.example.echo.R

class JoinGroupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_group)

        val spJoinGroupType = findViewById<Spinner>(R.id.spJoinGroupType)
        val spJoinGroupAge = findViewById<Spinner>(R.id.spJoinGroupAge)
        val spJoinGroupGender = findViewById<Spinner>(R.id.spJoinGroupGender)
        val spJoinGroupLevel = findViewById<Spinner>(R.id.spJoinGroupLevel)
        val spJoinGroupArea = findViewById<Spinner>(R.id.spJoinGroupArea)
        val imgJoinGroupFind = findViewById<ImageView>(R.id.imgJoinGroupFind)
        val rvJoinGroupList = findViewById<RecyclerView>(R.id.rvJoinGroupList)

        //스피너 값 초기화
        spJoinGroupType.adapter = ArrayAdapter.createFromResource(
            this,
            R.array.type,
            android.R.layout.simple_spinner_dropdown_item
        )

        spJoinGroupAge.adapter = ArrayAdapter.createFromResource(
            this,
            R.array.age,
            android.R.layout.simple_spinner_dropdown_item
        )

        spJoinGroupGender.adapter = ArrayAdapter.createFromResource(
            this,
            R.array.gender,
            android.R.layout.simple_spinner_dropdown_item
        )

        spJoinGroupLevel.adapter = ArrayAdapter.createFromResource(
            this,
            R.array.level,
            android.R.layout.simple_spinner_dropdown_item
        )

        spJoinGroupArea.adapter = ArrayAdapter.createFromResource(
            this,
            R.array.area,
            android.R.layout.simple_spinner_dropdown_item
        )



        imgJoinGroupFind.setOnClickListener{
            //찾기 버튼을 클릭했을 때

            //현재 스피너 선택 값 뽑아오기
            val type = spJoinGroupType.selectedItem.toString()
            val age = spJoinGroupAge.selectedItem.toString()
            val gender = spJoinGroupGender.selectedItem.toString()
            val level = spJoinGroupLevel.selectedItem.toString()
            val area = spJoinGroupArea.selectedItem.toString()

            val findGroup = FindGroupVO(type,age,gender,level,area)

            Log.d("값확인-모임찾기",findGroup.toString())



        }



    }//OnCreate 바깥
}