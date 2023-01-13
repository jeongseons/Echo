package com.example.echo.group.detail

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.echo.R
import com.example.echo.group.GroupListAdapter
import com.example.echo.group.GroupVO
import com.example.echo.group.NewDateVO
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import java.util.Date


class DetailDateFragment : Fragment() {

    var dateList = ArrayList<NewDateVO>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail_date, container, false)

        val cvGroupCalendar = view.findViewById<MaterialCalendarView>(R.id.cvGroupCalendar)
        val rvDetailGroupDate = view.findViewById<RecyclerView>(R.id.rvDetailGroupDate)
        val btnNewDate = view.findViewById<Button>(R.id.btnNewDate)

        dateList.add(NewDateVO("2023-01-13 07:00","상세설명"))
        dateList.add(NewDateVO("2023-01-13 07:00","상세설명"))

        val adapter = DetailDateAdapter(requireContext(),dateList)

        rvDetailGroupDate.adapter = adapter
        rvDetailGroupDate.layoutManager = LinearLayoutManager(requireContext())








        return view
    }

}