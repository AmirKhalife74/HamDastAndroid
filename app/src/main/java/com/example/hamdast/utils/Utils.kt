package com.example.hamdast.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.hamdast.data.models.calendar.CalendarDay
import com.example.hamdast.data.models.habit.HabitModel
import com.example.hamdast.data.models.habit.RepeatType
import java.text.DecimalFormat
import java.text.NumberFormat
import java.time.LocalDate

fun twoDigitConvertor(number: Int): String {
    return if (number < 10) {
        "0$number"
    } else {
        number.toString()
    }
}

var daysOfWeekInPersian:List<String> = listOf(
    "شنبه",
    "یکشنبه",
    "دوشنبه",
    "سه شنبه",
    "چهارشنبه",
    "پنج شنبه",
    "جمعه",
)

@RequiresApi(Build.VERSION_CODES.O)
fun CalendarDay.dayOfWeek(): Int {
    val localDate = LocalDate.of(year, month, day)
    val dow = localDate.dayOfWeek.value // 1=Monday .. 7=Sunday
    return when (dow) {
        7 -> 6 // Sunday -> جمعه
        else -> dow - 1 // Monday(1)->شنبه(0), Tuesday(2)->یکشنبه(1) ...
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


@RequiresApi(Build.VERSION_CODES.O)
fun HabitModel.shouldShowOn(day: CalendarDay): Boolean {
    return when (repeatType) {
        RepeatType.DAILY -> true

        RepeatType.WEEKLY -> daysOfWeek?.contains(day.dayOfWeek()) == true

        RepeatType.MONTHLY -> day.day == dayOfMonth

        RepeatType.YEARLY -> (day.month == monthOfYear && day.day == dayOfYear)

        RepeatType.CUSTOM -> {
            // مثال: هر X روز یکبار (با repeatInterval)
            val localDate = java.time.LocalDate.of(day.year, day.month, day.day)
            val startDate = LocalDate.of(day.year, 1, 1) // از اول سال شروع کنیم
            val daysSinceStart = java.time.temporal.ChronoUnit.DAYS.between(startDate, localDate)
            daysSinceStart % repeatInterval == 0L
        }
    }
}