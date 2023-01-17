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
import androidx.annotation.RequiresApi
import com.example.echo.R
import com.example.echo.group.NewDateVO
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class AddNewGroupDateActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_group_date)

        val tvNewGroupDateDate = findViewById<TextView>(R.id.tvNewGroupDateDate)
        val toDate = LocalDateTime.now()
        tvNewGroupDateDate.text =toDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        val imgNewGroupDate = findViewById<ImageView>(R.id.imgNewGroupDate)
        val mtNewGroupDateDetail = findViewById<EditText>(R.id.mtNewGroupDateDetail)
        val btnNewGroupDateAdd = findViewById<Button>(R.id.btnNewGroupDateAdd)


        imgNewGroupDate.setOnClickListener{
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            var dateString = ""
            var timeString = ""


            //시간 선택창 띄우는 부분
            val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->

                if(hourOfDay < 10){
                    timeString = "0${hourOfDay}:${minute}"
                    if(minute < 10){
                        timeString = "0${hourOfDay}:0${minute}"
                    }
                }else{
                    timeString = "${hourOfDay}:${minute}"
                    if(minute < 10){
                        timeString = "${hourOfDay}:0${minute}"
                    }
                }



                //사용자가 선택한 시간 텍스트 뷰에 적용
                tvNewGroupDateDate.text = "${dateString}${timeString}"
            }

            TimePickerDialog(this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),false).show()


            //날짜 선택창 띄우는 부분
            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                if(month>10){
                    dateString = "${year}-${month+1}-${dayOfMonth} "
                }else{
                    dateString = "${year}-0${month+1}-${dayOfMonth} "
                }

            }
            DatePickerDialog(this, dateSetListener, year, month, day).show()

        }

        btnNewGroupDateAdd.setOnClickListener{

        var addNewDetail = mtNewGroupDateDetail.text.toString()
        var date = tvNewGroupDateDate.text.toString()


        var addNewDateDate = NewDateVO(date,addNewDetail)

        Log.d("값확인", addNewDateDate.toString())

        }




    }



}