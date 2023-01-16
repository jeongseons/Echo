package com.example.echo.group.detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.echo.R
import com.example.echo.RetrofitBuilder
import com.example.echo.group.GroupVO
import com.example.echo.group.NewDateVO
import com.example.echo.group.detail.CalendarDecorate.TodayDecorate
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailDateFragment : Fragment() {

    lateinit var dateList : ArrayList<NewDateVO>
    lateinit var adapter : DetailDateAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail_date, container, false)

        val cvGroupCalendar = view.findViewById<MaterialCalendarView>(R.id.cvGroupCalendar)
        val rvDetailGroupDate = view.findViewById<RecyclerView>(R.id.rvDetailGroupDate)
        val btnNewDate = view.findViewById<Button>(R.id.btnNewDate)

        val todayDecorate = TodayDecorate()
        cvGroupCalendar.addDecorator(todayDecorate)



        //일정에서 날짜, 상세설명 불러와서 입력하는 부분분
        dateList.add(NewDateVO("2023-01-13 07:00","상세설명"))
        dateList.add(NewDateVO("2023-01-13 07:00","상세설명"))

        val adapter = DetailDateAdapter(requireContext(),dateList)

        rvDetailGroupDate.adapter = adapter
        rvDetailGroupDate.layoutManager = LinearLayoutManager(requireContext())



        //달력에 오늘 날짜 표시
        cvGroupCalendar.selectedDate = CalendarDay.today()
        val date = cvGroupCalendar.selectedDate

        Log.d("값확인-데이터",date.toString())


        return view
    }//OnCreate 바깥






}