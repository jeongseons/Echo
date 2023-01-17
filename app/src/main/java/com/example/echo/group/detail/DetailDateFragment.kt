package com.example.echo.group.detail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.echo.R
import com.example.echo.group.NewDateVO
import com.example.echo.group.detail.CalendarDecorate.*
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView


class DetailDateFragment : Fragment() {

    var dateList = ArrayList<NewDateVO>()
    lateinit var adapter : DetailDateAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail_date, container, false)

        val cvGroupCalendar = view.findViewById<MaterialCalendarView>(R.id.cvGroupCalendar)
        val rvDetailGroupDate = view.findViewById<RecyclerView>(R.id.rvDetailGroupDate)
        val btnNewDate = view.findViewById<Button>(R.id.btnNewDate)


        //평일 표시
        cvGroupCalendar.addDecorator(WeekDayDeco())
        //주말 표시
        cvGroupCalendar.addDecorator(SatDeco())
        cvGroupCalendar.addDecorator(SunDeco())
        //오늘 표시
        cvGroupCalendar.addDecorator(TodayDeco())
        //다음달 회색표시
        cvGroupCalendar.addDecorator(OtherMonthDeco())

        //일정에서 날짜, 상세설명 불러와서 입력하는 부분분
        dateList.add(NewDateVO("2023-01-13 07:00","상세설명",76))
        dateList.add(NewDateVO("2023-01-14 07:00","상세설명",76))

        val adapter = DetailDateAdapter(requireContext(),dateList)

        rvDetailGroupDate.adapter = adapter
        rvDetailGroupDate.layoutManager = LinearLayoutManager(requireContext())



        //달력에 오늘 날짜 표시
        cvGroupCalendar.selectedDate = CalendarDay(2023,0,17)
        cvGroupCalendar.selectedDate = CalendarDay(2023,0,18)



        btnNewDate.setOnClickListener{
            val intent = Intent(context, AddNewGroupDateActivity::class.java)
            startActivity(intent)

        }


        return view
    }//OnCreate 바깥






}