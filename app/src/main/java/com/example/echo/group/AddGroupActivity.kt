package com.example.echo.group

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import com.example.echo.R
import com.example.echo.RetrofitBuilder
import okhttp3.ResponseBody
import org.apache.commons.lang3.math.NumberUtils.toInt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar
import java.util.Date


class AddGroupActivity : AppCompatActivity() {

    lateinit var datePickerDialog: DatePickerDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_group)


        //------뷰 초기화--------
        val btnMoveBack = findViewById<Button>(R.id.btnAddGroupBack)
        //그룹 프로필사진
        val imgGroupProfile = findViewById<ImageView>(R.id.imgAddGroup)
        //그룹명
        val etAddGroupName = findViewById<EditText>(R.id.etAddGroupName)
        //인원
        val spAddGroupMax = findViewById<Spinner>(R.id.spAddGroupMax)
        //분류
        val ckAddGroupType1 = findViewById<CheckBox>(R.id.ckAddGroupType1)
        val ckAddGroupType2 = findViewById<CheckBox>(R.id.ckAddGroupType2)
        //기본값으로 산행
        ckAddGroupType1.isChecked=true
        //지역
        val spAddGroupArea = findViewById<Spinner>(R.id.spAddGroupArea)
        //성별
        val spAddGroupGender = findViewById<Spinner>(R.id.spAddGroupGender)
        //연령
        val spAddGroupAge = findViewById<Spinner>(R.id.spAddGroupAge)
        //난이도
        val spAddGroupLevel = findViewById<Spinner>(R.id.spAddGroupLevel)
        //산행일자
        val tvAddGroupDate = findViewById<TextView>(R.id.tvAddGroupDate)
        val imgAddGroupCalendar = findViewById<ImageView>(R.id.imgAddGroupCalendar)
        //일정 조정 여부
        val swAddGroupYn = findViewById<Switch>(R.id.swAddGroupYn)
        //세부일정
        val mtAddGroupDetailDate = findViewById<EditText>(R.id.mtAddGroupDetailDate)
        //동의
        val ckAddGroupAgreement1 = findViewById<CheckBox>(R.id.ckAddGroupAgreement1)
        val ckAddGroupAgreement2 = findViewById<CheckBox>(R.id.ckAddGroupAgreement2)
        //기본 동의 체크
        ckAddGroupAgreement1.isChecked=true
        //모임생성
        val btnAddGroupAdd = findViewById<Button>(R.id.btnAddGroupAdd)
        //모임명 공백 체크
        var nameCk = false
        //동의 여부 체크
        var agree = true
        //세부일정 공백 체크
        var detailCk = false

        var name:String
        var date:String
        var detail:String
        //스피너 값 변수에 저장
        var age:String
        var area:String
        var max :Int
        var gender:String
        var lev:String
        //산행 분류 선택
        var type:String




        //뒤로가기 클릭 시 이전 페이지
        btnMoveBack.setOnClickListener{
            finish()
        }


        //Spinner 아이템 리스트 설정
        //인원
        spAddGroupMax.adapter = ArrayAdapter.createFromResource(this,
            R.array.max,
            android.R.layout.simple_spinner_dropdown_item)

        //지역
        spAddGroupArea.adapter = ArrayAdapter.createFromResource(this,
            R.array.area,
            android.R.layout.simple_spinner_dropdown_item)
        //성별
        spAddGroupGender.adapter = ArrayAdapter.createFromResource(this,
            R.array.gender,
            android.R.layout.simple_spinner_dropdown_item)

        //연령
        spAddGroupAge.adapter = ArrayAdapter.createFromResource(this,
            R.array.age,
            android.R.layout.simple_spinner_dropdown_item)

        //난이도
        spAddGroupLevel.adapter = ArrayAdapter.createFromResource(this,
        R.array.level,
        android.R.layout.simple_spinner_dropdown_item)




        //클릭 시 달력 띄우기
        imgAddGroupCalendar.setOnClickListener{
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            var dateString = ""
            var timeString = ""
            var amPm = ""


            //시간 선택창 띄우는 부분
            val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->

                //AM / PM 구분하는 부분
                if(hourOfDay < 12) {
                    if(hourOfDay < 10){
                        amPm = "AM";
                        timeString = "0${hourOfDay}시 ${minute}분"
                    }else{
                        amPm = "AM";
                        timeString = "${hourOfDay}시 ${minute}분"
                    }

                } else {
                    amPm = "PM";
                    if(hourOfDay == 12){
                        timeString = "${hourOfDay}시 ${minute}분"
                    }else{
                        timeString = "${hourOfDay-12}시 ${minute}분"
                    }


                }

                //사용자가 선택한 시간 텍스트 뷰에 적용
                tvAddGroupDate.text = "${dateString} ${amPm}${timeString}"
            }

            TimePickerDialog(this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),false).show()


            //날짜 선택창 띄우는 부분
            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                dateString = "${year}-${month+1}-${dayOfMonth} "

            }
            DatePickerDialog(this, dateSetListener, year, month, day).show()



        }

        //동의 체크박스 한 쪽만 선택 가능
        ckAddGroupAgreement1.setOnClickListener(){
            if(ckAddGroupAgreement1.isChecked){
                ckAddGroupAgreement2.isChecked=false
            }else if(ckAddGroupAgreement2.isChecked){
                ckAddGroupAgreement1.isChecked=false
            }
        }

        ckAddGroupAgreement2.setOnClickListener(){
            if(ckAddGroupAgreement2.isChecked){
                ckAddGroupAgreement1.isChecked=false
            }else if(ckAddGroupAgreement1.isChecked){
                ckAddGroupAgreement2.isChecked=false
            }
        }






        //모임 생성 버튼을 클릭했을 때
        btnAddGroupAdd.setOnClickListener{
            //사용자 입력값 저장
            name = etAddGroupName.text.toString()
            date = tvAddGroupDate.text.toString()
            detail = mtAddGroupDetailDate.text.toString()
            //스피너 값 변수에 저장
            age = spAddGroupAge.selectedItem.toString()
            area = spAddGroupArea.selectedItem.toString()
            max = toInt(spAddGroupMax.selectedItem.toString())
            gender = spAddGroupGender.selectedItem.toString()
            lev = spAddGroupLevel.selectedItem.toString()
            //산행 분류 선택
            var type = ""

            if(ckAddGroupType1.isChecked){
                type="등산"
                if(ckAddGroupType1.isChecked&&ckAddGroupType2.isChecked){
                    type="등산/등반"
                }
            }else if(ckAddGroupType2.isChecked){
                type="등반"

            }
            
            //모임명 Null체크
            if(name == ""){
                Toast.makeText(this,"모임명을 입력하세요",Toast.LENGTH_SHORT).show()
            }else{
                nameCk = true
            }

            //동의 여부 체크
            if(ckAddGroupAgreement2.isChecked){
                Toast.makeText(this,"필수 동의가 필요합니다",Toast.LENGTH_SHORT).show()
                agree = false
            }

            //세부일정 Null체크
            if(detail == ""){
                Toast.makeText(this,"세부 일정을 입력하세요",Toast.LENGTH_SHORT).show()
                detailCk = false
            }else{
                detailCk = true
            }

            //조건 만족 시 그룹 생성
            if(agree==true&&nameCk==true&&detailCk==true){
                //New 그룹 VO에 값 담기
                val newGroup = NewGroupVO(intent.getStringExtra("user")!!,
                    "1234",name,area,max,age,lev,date,
                    swAddGroupYn.isChecked,gender,type,detail)

                Log.d("값확인VO",newGroup.toString())
                addGroup(newGroup)
            }

        }//모임 생성

    }//onCreate 바깥

    //그룹 추가 - 스프링 통신
    fun addGroup(group:NewGroupVO){
        val call = RetrofitBuilder.api.addGroup(group)
        call.enqueue(object : Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.d("확인",response.body().toString())
                Toast.makeText(this@AddGroupActivity,"모임이 생성되었습니다.",Toast.LENGTH_LONG)
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@AddGroupActivity,"가입한 모임이 없습니다!",Toast.LENGTH_LONG)
                Log.d("저장실패", t.localizedMessage)
            }
        })
    }


}