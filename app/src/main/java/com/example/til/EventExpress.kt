package com.example.til

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan

class EventExpress(context: Context, eventList: ArrayList<CalendarDay>) : DayViewDecorator {

    private var date= HashSet<CalendarDay>(eventList)
    override fun shouldDecorate(day: CalendarDay?): Boolean {
            return date.contains(day)
    }

    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(DotSpan(7F, Color.BLACK))
    }

}