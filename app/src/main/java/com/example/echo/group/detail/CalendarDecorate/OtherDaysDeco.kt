package com.example.echo.group.detail.CalendarDecorate

import android.graphics.Color
import android.text.style.ForegroundColorSpan
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class OtherDaysDeco (select: Int) : DayViewDecorator {

    val selectMonth = select

    override fun shouldDecorate(day: CalendarDay?): Boolean {

        return (day?.month != selectMonth && day!!.day > 1)
                || (day?.month != selectMonth && day!!.day < 31)
    }

    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(object : ForegroundColorSpan(Color.parseColor("#DBDBDB")) {})
    }
}