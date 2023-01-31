package com.example.echo.group

import android.app.Activity

import android.os.Bundle
import android.widget.*
import com.example.echo.R


class EditGroupInfoActivity : Activity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_group_info)

        val imgEditGroupInfo = findViewById<ImageView>(R.id.imgEditGroupInfo)
        val imgEditGroupInfoEdit = findViewById<ImageView>(R.id.imgEditGroupInfoEdit)
        val editTextTextPersonName = findViewById<EditText>(R.id.editTextTextPersonName)
        val ckEditGroupInfoType1 = findViewById<CheckBox>(R.id.ckEditGroupInfoType1)
        val ckEditGroupInfoType2 = findViewById<CheckBox>(R.id.ckEditGroupInfoType2)

        //스피너 값 적용
        val spEditGroupInfoMax = findViewById<Spinner>(R.id.spEditGroupInfoMax)
        spEditGroupInfoMax.adapter = ArrayAdapter.createFromResource(this,
            R.array.max,
            android.R.layout.simple_spinner_dropdown_item)

        val spEditGroupInfoArea = findViewById<Spinner>(R.id.spEditGroupInfoArea)
        spEditGroupInfoArea.adapter = ArrayAdapter.createFromResource(this,
            R.array.area,
            android.R.layout.simple_spinner_dropdown_item)

        val spEditGroupInfoGender = findViewById<Spinner>(R.id.spEditGroupInfoGender)
        spEditGroupInfoGender.adapter = ArrayAdapter.createFromResource(this,
            R.array.gender,
            android.R.layout.simple_spinner_dropdown_item)

        val spEditGroupInfoAge = findViewById<Spinner>(R.id.spEditGroupInfoAge)
        spEditGroupInfoAge.adapter = ArrayAdapter.createFromResource(this,
            R.array.age,
            android.R.layout.simple_spinner_dropdown_item)

        val spEditGroupInfoLevel = findViewById<Spinner>(R.id.spEditGroupInfoLevel)
        spEditGroupInfoLevel.adapter = ArrayAdapter.createFromResource(this,
            R.array.level,
            android.R.layout.simple_spinner_dropdown_item)

        val etEditGroupInfoDetail = findViewById<EditText>(R.id.etEditGroupInfoDetail)
        val btnEditGroupInfoSet = findViewById<Button>(R.id.btnEditGroupInfoSet)







    }
}