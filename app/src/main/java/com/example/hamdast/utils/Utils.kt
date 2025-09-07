package com.example.hamdast.utils

import com.example.hamdast.data.models.CalendarDay
import com.example.hamdast.data.models.HabitModel
import com.example.hamdast.data.models.RepeatType
import java.text.DecimalFormat
import java.text.NumberFormat

fun twoDigitConvertor(number: Int): String {
    return if (number < 10) {
        "0$number"
    } else {
        number.toString()
    }
}

fun moneySeperator(number: Long): String {
    var num = number
    if (num < 0) {
        num = num * (-1)
    }

    val formatter: NumberFormat = DecimalFormat("#,###")
    return formatter.format(num)
}

fun HabitModel.shouldShowOn(day: CalendarDay): Boolean {
    return when (repeatType) {
        RepeatType.DAILY -> true
        RepeatType.WEEKLY -> daysOfWeek?.contains(day.dayOfWeek()) ?: false
        RepeatType.MONTHLY -> day.day == dayOfMonth
        RepeatType.YEARLY -> (day.month == monthOfYear && day.day == dayOfYear)
        RepeatType.CUSTOM -> {
            // مثال: روزهای زوج
            day.day % repeatInterval == 0
        }
    }
}