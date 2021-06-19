package com.example.teamproject.custom

import android.graphics.Color
import android.graphics.Typeface
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import java.util.*

class CustomDecorator(
    var dayOfTheWeek: String, var setting: String, var color: Int
) : DayViewDecorator {

    private val calendar: Calendar = Calendar.getInstance()
    private var date = CalendarDay.today()

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        day?.copyTo(calendar)
        val weekDay: Int = calendar.get(Calendar.DAY_OF_WEEK)
        return when (dayOfTheWeek) {
            "saturday" -> {
                weekDay == Calendar.SATURDAY
            }
            "sunday" -> {
                weekDay == Calendar.SUNDAY
            }
            else -> {
                day?.equals(date)!!
            }
        }
    }

    override fun decorate(view: DayViewFacade?) {
        if (setting == "off") {
            view?.addSpan(ForegroundColorSpan(Color.BLACK))
        } else {
            when (dayOfTheWeek) {
                "saturday" -> {
                    view?.addSpan(ForegroundColorSpan(Color.BLUE))
                }
                "sunday" -> {
                    view?.addSpan(ForegroundColorSpan(Color.RED))
                }
                else -> {
                    view?.addSpan(StyleSpan(Typeface.BOLD))
                    view?.addSpan(DotSpan(5f, Color.RED))
                }
            }
        }
    }
}