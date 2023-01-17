package com.example.echo.group.detail.CalendarDecorate

import android.graphics.Color
import android.text.style.ForegroundColorSpan
import android.util.Log
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import java.util.*

class OtherMonthDeco: DayViewDecorator {

    private val calendar = Calendar.getInstance()

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        day?.copyTo(calendar)
        val otherMonth = calendar.get(Calendar.MONTH)
        Log.d("값출력-달력",Calendar.MONTH.toString())
        return otherMonth != Calendar.MONTH

    }

    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(object : ForegroundColorSpan(Color.parseColor("#dbdbdb")) {})
    }

}