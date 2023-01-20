package com.example.echo.group.detail

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.echo.R
import com.example.echo.RetrofitBuilder
import com.example.echo.group.NewDateVO
import com.example.echo.group.NewGroupVO
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class AddNewGroupDateActivity : AppCompatActivity() {


    lateinit var addNewDateDate:NewDateVO

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_group_date)

        val tvNewGroupDateDate = findViewById<TextView>(R.id.tvNewGroupDateDate)

        //사용자가 달력에서 선택한 날짜
        val getDate = intent.getStringExtra("date")

        Log.d("값확인-가져온날짜", getDate.toString())


        val toDate = LocalDateTime.now()
        if (getDate != null) {
            val date = LocalDateTime.now()
            var hour = date.hour.toString()
            var min = date.minute.toString()
            if (hour.toInt() < 10) hour = "0$hour"
            if (min.toInt() < 10) min = "0$min"
            tvNewGroupDateDate.text = "${getDate} ${hour}:${min}"
        } else {
            tvNewGroupDateDate.text = toDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        }
        val imgNewGroupDate = findViewById<ImageView>(R.id.imgNewGroupDate)
        val mtNewGroupDateDetail = findViewById<EditText>(R.id.mtNewGroupDateDetail)
        val btnNewGroupDateAdd = findViewById<Button>(R.id.btnNewGroupDateAdd)


        imgNewGroupDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            var dateString = ""
            var timeString = ""




            //시간 선택창 띄우는 부분
            val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->

                if (hourOfDay < 10) {
                    timeString = "0${hourOfDay}:${minute}"
                    if (minute < 10) {
                        timeString = "0${hourOfDay}:0${minute}"
                    }
                } else {
                    timeString = "${hourOfDay}:${minute}"
                    if (minute < 10) {
                        timeString = "${hourOfDay}:0${minute}"
                    }
                }

                //사용자가 선택한 시간 텍스트 뷰에 적용
                tvNewGroupDateDate.text = "${dateString}${timeString}"
            }

            TimePickerDialog(
                this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), false
            ).show()


            //날짜 선택창 띄우는 부분
            val dateSetListener =
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    if (month > 10) {
                        dateString = "${year}-${month + 1}-${dayOfMonth} "
                        if (dayOfMonth < 10) {
                            dateString = "${year}-${month + 1}-0${dayOfMonth} "
                        }
                    } else {
                        dateString = "${year}-0${month + 1}-${dayOfMonth} "
                        if (dayOfMonth < 10) {
                            dateString = "${year}-0${month + 1}-0${dayOfMonth} "
                        }
                    }

                }
            if(getDate != null){
                val year = intent.getStringExtra("year")!!.toInt()
                val month = intent.getStringExtra("month")!!.toInt()
                val day = intent.getStringExtra("day")!!.toInt()
                Log.d("값확인-가져온날짜(합)","${year}-${month}-${day}")
                DatePickerDialog(this, dateSetListener, year, month-1,day).show()
            }else {
                DatePickerDialog(this, dateSetListener, year, month, day).show()
            }

        }

        btnNewGroupDateAdd.setOnClickListener {

            //사용자 입력값 불러오기
            var addNewDetail = mtNewGroupDateDetail.text.toString()
            var getDate = tvNewGroupDateDate.text.toString()


            //저장된 그룹 번호 불러오기
            val sharedPreferences = getSharedPreferences("group_seq", 0)
            var group_seq = sharedPreferences.getInt("group_seq", 0).toInt()
            Log.d("값확인-그룹번호", group_seq.toString())

            addNewDateDate = NewDateVO(getDate, addNewDetail, group_seq)

            //상세일정이 null 이 아닐 시 데이터 값 출력 후 종료

            if (addNewDetail != "") {
                Log.d("값확인", addNewDateDate.toString())
                addCal(addNewDateDate)
                finish()

            } else {
                Toast.makeText(this, "상세일정을 입력하세요", Toast.LENGTH_SHORT).show()
            }


        }




    }

    fun addCal(cal: NewDateVO){
        val call = RetrofitBuilder.api.addCal(cal)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.d("확인",response.body().toString())
                Toast.makeText(applicationContext,"일정이 생성되었습니다.",Toast.LENGTH_LONG)
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("저장실패", t.localizedMessage)
            }
        })
    }




}