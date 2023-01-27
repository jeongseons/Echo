package com.example.echo.group.detail.CalendarDecorate

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import com.example.echo.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class TodayDeco(context: Context):DayViewDecorator {
    private var date = CalendarDay.today()
    val drawable = context.resources.getDrawable(R.drawable.calendar_select)
    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return day?.equals(date)!!
    }
    override fun decorate(view: DayViewFacade?) {
        view?.setBackgroundDrawable(drawable)
    }
}