package com.example.echo.group

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable

import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.echo.R
import com.example.echo.RetrofitBuilder
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import okhttp3.ResponseBody
import org.apache.commons.lang3.math.NumberUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.time.format.DateTimeFormatter


lateinit var imgEditGroupInfo : ImageView

class EditGroupInfoActivity : Activity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_group_info)

        // 뷰 초기화
        imgEditGroupInfo = findViewById(R.id.imgEditGroupInfo)
        val imgEditGroupInfoEdit = findViewById<ImageView>(R.id.imgEditGroupInfoEdit)
        val editTextTextPersonName = findViewById<EditText>(R.id.editTextTextPersonName)
        val ckEditGroupInfoType1 = findViewById<CheckBox>(R.id.ckEditGroupInfoType1)
        val ckEditGroupInfoType2 = findViewById<CheckBox>(R.id.ckEditGroupInfoType2)
        val etEditGroupInfoDetail = findViewById<EditText>(R.id.etEditGroupInfoDetail)

        val btnEditGroupInfoSet = findViewById<Button>(R.id.btnEditGroupInfoSet)
        val imgEditGroupInfoMoveBack = findViewById<ImageView>(R.id.imgEditGroupInfoMoveBack)

        // 뒤로가기
        imgEditGroupInfoMoveBack.setOnClickListener {
            finish()
        }

        //모임명 공백 체크
        var nameCk = false
        //세부일정 공백 체크
        var detailCk = false

        var name:String
        var detail:String
        //스피너 값 변수에 저장
        var age:String
        var area:String
        var max :Int
        var gender:String
        var lev:String
        //산행 분류 선택
        var type:String


        // 기존 설정값 불러오기
        val group_profile_img = intent.getStringExtra("group_profile_img")
        val group_name = intent.getStringExtra("group_name")
        val group_area = intent.getStringExtra("group_area")
        val user_max = intent.getStringExtra("user_max")
        val group_age = intent.getStringExtra("group_age")
        val group_level = intent.getStringExtra("group_level")
        var group_gender = intent.getStringExtra("group_gender")
        var group_type = intent.getStringExtra("group_type")
        var group_detail = intent.getStringExtra("group_detail")
        var group_seq = intent.getIntExtra("group_seq", 0)

        editTextTextPersonName.setText(group_name)
        etEditGroupInfoDetail.setText(group_detail)

        if(group_type=="등산") ckEditGroupInfoType1.isChecked = true
        if(group_type=="등반") ckEditGroupInfoType2.isChecked = true
        when (group_type) {
            "등산" -> ckEditGroupInfoType1.isChecked = true
            "등반" -> ckEditGroupInfoType2.isChecked = true
            else -> {ckEditGroupInfoType1.isChecked = true
            ckEditGroupInfoType2.isChecked = true}
        }

        Glide.with(this)
            .load(group_profile_img)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(imgEditGroupInfo)

        Log.d("test-그룹정보수정", group_profile_img!!)

        imgEditGroupInfoEdit.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI
            )
            launcher.launch(intent)
        }

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


        btnEditGroupInfoSet.setOnClickListener {
            //사용자 입력값 저장
            name = editTextTextPersonName.text.toString()
            detail = etEditGroupInfoDetail.text.toString()
            //스피너 값 변수에 저장
            age = spEditGroupInfoAge.selectedItem.toString()
            area = spEditGroupInfoArea.selectedItem.toString()
            max = NumberUtils.toInt(spEditGroupInfoMax.selectedItem.toString())
            gender = spEditGroupInfoGender.selectedItem.toString()
            lev = spEditGroupInfoLevel.selectedItem.toString()
            //산행 분류 선택
            var type = ""
            // 그룹 프로필 이미지 설정
            var key = "${name}${group_seq}"
            var profile = "https://firebasestorage.googleapis.com/v0/b/echo-73cf6.appspot.com/o/${key}.png?alt=media"

            if(ckEditGroupInfoType1.isChecked){
                type="등산"
                if(ckEditGroupInfoType1.isChecked&&ckEditGroupInfoType2.isChecked){
                    type="등산/등반"
                }
            }else if(ckEditGroupInfoType2.isChecked){
                type="등반"

            }

            //모임명 Null체크
            if(name == ""){
                Toast.makeText(this,"모임명을 입력하세요",Toast.LENGTH_SHORT).show()
            }else{
                nameCk = true
            }

            //모임설명 Null체크
            if(detail == ""){
                Toast.makeText(this,"모임 설명을 입력하세요",Toast.LENGTH_SHORT).show()
                detailCk = false
            }else{
                detailCk = true
            }

            //조건 만족 시 그룹 생성
            if(nameCk==true&&detailCk==true){
                //New 그룹 VO에 값 담기
                val settingGroup = SettingGroupVO(group_seq,
                    profile,name,area,max,age,lev,gender,type,detail)

                Log.d("값확인VO",settingGroup.toString())
                modifyGroup(settingGroup)
                imgUpload(key)

                finish()
            }
        }


    }

    val launcher = AppCompatActivity().registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        //받아올 결과값이 맞는지 확인 과정
        if (it.resultCode == RESULT_OK) imgEditGroupInfo.setImageURI(it.data?.data)
    }

    fun imgUpload(key : String) {

        val storage = Firebase.storage
        val storageRef = storage.reference
        val mountainsRef = storageRef.child("$key.png")

        imgEditGroupInfo.isDrawingCacheEnabled = true
        imgEditGroupInfo.buildDrawingCache()
        val bitmap = (imgEditGroupInfo.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        //quality:압축 퀄리티 1~100.
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos)
        val data = baos.toByteArray()

        //mountainsRef : 스토리지 경로 지정하는 키워드.
        var uploadTask = mountainsRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }
    }

    fun modifyGroup(settingGroup: SettingGroupVO){
        val call = RetrofitBuilder.api.modifyGroup(settingGroup)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>
            ) {
                Toast.makeText(
                    applicationContext, "수정되었습니다",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("test-글수정실패", t.localizedMessage)
            }
        })
    }

}