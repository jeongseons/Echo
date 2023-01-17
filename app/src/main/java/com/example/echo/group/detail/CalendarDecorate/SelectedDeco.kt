package com.example.echo.group.detail.CalendarDecorate

import android.app.Activity
import android.graphics.drawable.Drawable
import com.example.echo.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class SelectedDeco(context: Activity?):DayViewDecorator {

    private val drawable: Drawable?

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return true
    }

    override fun decorate(view: DayViewFacade?) {
        if (drawable != null) {
            view?.setSelectionDrawable(drawable)
        }
    }

    init {
        drawable = context!!.getResources().getDrawable(R.drawable.selected_day)
    }
}