package com.example.echo.group.detail

import EventDeco
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.echo.R
import com.example.echo.RetrofitBuilder
import com.example.echo.group.GroupVO
import com.example.echo.group.NewDateVO
import com.example.echo.group.detail.CalendarDecorate.*
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Closeable
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class DetailDateFragment : Fragment() {

    var dateList = ArrayList<NewDateVO>()
    lateinit var adapter : DetailDateAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail_date, container, false)

        val cvGroupCalendar = view.findViewById<MaterialCalendarView>(R.id.cvGroupCalendar)
        val rvDetailGroupDate = view.findViewById<RecyclerView>(R.id.rvDetailGroupDate)
        val btnNewDate = view.findViewById<Button>(R.id.btnNewDate)
        val seq = requireActivity().intent.getIntExtra("num", 0)

        //초기 month 셋팅
        val getMonth = Calendar.getInstance().time
        val month = SimpleDateFormat("MM",Locale.KOREA).format(getMonth)

        //평일 표시
        cvGroupCalendar.addDecorator(WeekDayDeco())
        //주말 표시
        cvGroupCalendar.addDecorator(SatDeco())
        cvGroupCalendar.addDecorator(SunDeco())
        //오늘 표시
        cvGroupCalendar.addDecorator(TodayDeco(requireContext()))
        //다른 달 날짜 표기
        cvGroupCalendar.addDecorator(OtherDaysDeco(month.toInt()-1))

        //Dot 색상 선택
        val colorArray:Array<String> = arrayOf("#FF0000")

        cvGroupCalendar.addDecorator(EventDeco
            (requireContext(),colorArray,CalendarDay(2023,0,20)))
        cvGroupCalendar.addDecorator(EventDeco
            (requireContext(),colorArray,CalendarDay(2023,0,22)))





        //달력 이동시 데코레이션 적용
        cvGroupCalendar.setOnMonthChangedListener { widget, date ->
            var month = (date.month + 1).toString()

            if (month.toInt() < 10) month = "0$month"

            //실행 전 데코 초기화
            cvGroupCalendar.removeDecorators()
            //평일 표시
            cvGroupCalendar.addDecorator(WeekDayDeco())
            //주말 표시
            cvGroupCalendar.addDecorator(SatDeco())
            cvGroupCalendar.addDecorator(SunDeco())
            //오늘 표시
            cvGroupCalendar.addDecorator(TodayDeco(requireContext()))
            //다른 달 날짜 표기
            cvGroupCalendar.addDecorator(OtherDaysDeco(month.toInt()-1))

        }

        GetCalList(seq)


        adapter = DetailDateAdapter(requireContext(),dateList)


        rvDetailGroupDate.adapter = adapter
        rvDetailGroupDate.layoutManager = LinearLayoutManager(requireContext())





        btnNewDate.setOnClickListener{
            val selectedDate = cvGroupCalendar.selectedDate

            val intent = Intent(context, AddNewGroupDateActivity::class.java)
            if(selectedDate != null){
                val selectedYear = selectedDate.year
                var selectedMonth = selectedDate.month+1
                val selectedDay = selectedDate.day

                intent.putExtra("year",selectedYear)
                intent.putExtra("month",selectedMonth)
                intent.putExtra("day",selectedDay)

                
                if(selectedMonth>10){
                    intent.putExtra("date","${selectedYear}-${selectedMonth}-${selectedDay}")
                    if(selectedDay>10){
                        intent.putExtra("date","${selectedYear}-${selectedMonth}-0${selectedDay}")
                    }
                }else{
                    intent.putExtra("date","${selectedYear}-0${selectedMonth}-${selectedDay}")
                    if(selectedDay<10){
                        intent.putExtra("date","${selectedYear}-0${selectedMonth}-0${selectedDay}")
                    }
                }

                Log.d("값확인-보내는날짜","${selectedYear}-${selectedMonth}-${selectedDay}")
            }
            startActivity(intent)

        }


        return view
    }//OnCreate 바깥

    fun GetCalList(seq: Int) {//그룹 리스트 - 스프링 통신
        val call = RetrofitBuilder.api.getCalList(seq)
        call.enqueue(object : Callback<List<NewDateVO>> {

            override fun onResponse(call: Call<List<NewDateVO>>, response: Response<List<NewDateVO>>) {

                if (response.isSuccessful) {//성공
                    Log.d("rty",response.body().toString())
                    dateList.clear()
                    if(response.body()?.size!=0) {//가입한 그룹이 있을 때
                        for (i: Int in 0 until response.body()!!.size) {
                            //그룹리스트 정보 담아줌.
                            dateList.add(
                                NewDateVO(
                                    response.body()!!.get(i).cal_dt,
                                    response.body()!!.get(i).cal_content,
                                    seq
                                )
                            )
                        }
                        //리스트 추가후 어댑터 새로고침 필수!
                        // 근본적인 원인 : API 호출 이후 새로고침이 일어나야 하는데
                        // 새로고침이 일어난 이후에 데이터가 쌓임
                        adapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call<List<NewDateVO>>, t: Throwable) {
                Log.d("불러오기실패", t.localizedMessage)
            }

        })
    }




}