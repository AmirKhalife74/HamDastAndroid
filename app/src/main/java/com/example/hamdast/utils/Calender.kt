package com.example.hamdast.utils

import com.example.hamdast.data.models.CalendarDay
import java.text.SimpleDateFormat
import java.util.Calendar

val DAYS_IN_MILLLIS = 86400000
val WEEK_IN_MILLIS = DAYS_IN_MILLLIS * 7
val YEAR_IN_MILLIS: Long = DAYS_IN_MILLLIS * 365L
val daysInMonth = listOf(31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 29)
val leapYears = listOf(
    1300, 1304, 1309, 1313, 1317, 1321, 1325, 1329, 1333, 1337, 1342, 1346, 1350, 1354, 1358, 1362,
    1366, 1370, 1375, 1379, 1383, 1387, 1391, 1395, 1399, 1403, 1408, 1412, 1416, 1420, 1424, 1428,
    1432, 1432, 1436, 1441, 1445, 1449, 1453, 1457, 1461, 1465, 1469, 1474, 1478, 1482, 1486, 1490,
    1494, 1498
)
val persionMonth = listOf(
    "فروردین", "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور",
    "مهر", "آبان", "آذر", "دی", "بهمن", "اسفند"
)
val persionDateText = listOf(
    "اول", "دوم", "سوم", "چهارم", "پنجم", "ششم", "هفتم",
    "هشتم", "نهم", "دهم", "یازدهم", "دوازدهم", "سیزدهم", "چهاردهم", "پانزدهم", "شانزدهم", "هفدهم",
    "هجدهم", "نوزدهم", "بیستم", "بیست و یکم", "بیست و دوم", "بیست و سوم", "بیست و چهارم",
    "بیست و پنجم", "بیست و ششم", "بیست و هفتم", "بیست و هشتم", "بیست و نهم", "سی ام", "سی و یکم"
)
val dayOfWeekEn =
    listOf("Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday")
val dayOfWeekFa = listOf("شنبه", "یکشنبه", "دوشنبه", "سه شنبه", "چهارشنبه", "پنجشنبه", "جمعه")

fun getMillisFromDate(date: String, format: String): Long {
    try {
        val sdf = SimpleDateFormat(format)
        val mdate = sdf.parse(date)
        return mdate.time+86400000
    } catch (e: Exception) {

    }
    return 0
}

fun G2JFromDate(date: String, format: String, wanted: G2JTypes): String {
    return G2JFromMillis(getMillisFromDate(date, format), wanted)
}

fun G2JFromMillis(millies: Long, wanted: G2JTypes): String {
    var monthIndex = 9
    var year = 1348
    var day = 11
    var daysPassed = millies / DAYS_IN_MILLLIS
    var remainingTime = millies % DAYS_IN_MILLLIS
    if (daysPassed > 0) {
        while (daysPassed != 0L) {
            if (day < daysInMonth[monthIndex]) {
                day++
                daysPassed--
            } else if (day > daysInMonth[monthIndex]) {
                monthIndex = 0
                year++
                day = 1
                daysPassed--
            } else {
                if (monthIndex < 11) {
                    day = 1
                    monthIndex++
                } else if (leapYears.contains(year)) {
                    day = 30
                } else {
                    day = 1
                    monthIndex = 0
                    year++
                }
                daysPassed--
            }

        }
    }
    when (wanted) {
        G2JTypes.AGE -> {
            var currentYearString = G2JFromMillis(System.currentTimeMillis(), G2JTypes.YEAR)
            var currentYear = currentYearString.toInt()
            return "${currentYear - year}"
        }
        G2JTypes.YEAR -> {
            return year.toString()
        }
        G2JTypes.MONTH -> {
            return twoDigitConvertor(monthIndex + 1)
        }
        G2JTypes.DAY -> {
            return twoDigitConvertor(day)
        }
        G2JTypes.DISTANCECALC -> {
            val now = System.currentTimeMillis()
            val todayYear = G2JFromMillis(now, G2JTypes.YEAR).toInt()
            val todayMonth = G2JFromMillis(now, G2JTypes.MONTH).toInt()
            val todayDay = G2JFromMillis(now, G2JTypes.DAY).toInt()
            val calculatedYear = year
            val calculatedMonth = monthIndex + 1
            val calculatedDay = day
            if (todayYear == calculatedYear && todayMonth == calculatedMonth) {
                if (todayDay == calculatedDay) {
                    val hour = remainingTime / 3600000
                    val min = (remainingTime % 3600000) / 60000
                    val sec = ((remainingTime % 3600000) % 60000) / 1000
                    return "${twoDigitConvertor(hour.toInt())} : ${twoDigitConvertor(min.toInt())}"
                } else if (todayDay - 1 == calculatedDay) {
                    return "دیروز"
                } else {
                    return "${year}/${twoDigitConvertor(monthIndex + 1)}/${twoDigitConvertor(day)}"
                }
            } else {
                return "${year}/${twoDigitConvertor(monthIndex + 1)}/${twoDigitConvertor(day)}"
            }

        }
        G2JTypes.FULL -> {
            val hour = remainingTime / 3600000
            val min = (remainingTime % 3600000) / 60000
            val sec = ((remainingTime % 3600000) % 60000) / 1000
            return "${year}/${twoDigitConvertor(monthIndex + 1)}/${twoDigitConvertor(day)} ${
                twoDigitConvertor(
                    hour.toInt()
                )
            }:${
                twoDigitConvertor(
                    min.toInt()
                )
            }:${
                twoDigitConvertor(
                    sec.toInt()
                )
            }"
        }
        G2JTypes.DATE -> {
            return "${year}/${twoDigitConvertor(monthIndex + 1)}/${twoDigitConvertor(day)}"
        }
        G2JTypes.MONTHINYEAR -> {
            return "${persionMonth.get(monthIndex)} ماه ${year}"
        }
        G2JTypes.PERSIANMONTH -> {
            return "${persionMonth.get(monthIndex)}"
        }
        G2JTypes.MONTHLENGTH -> {
            return "${daysInMonth.get(monthIndex)}"
        }

    }
}

fun persianToGregorian(year: Int, month: Int, day: Int): Triple<Int, Int, Int> {
    val gDaysInMonth = intArrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
    val jDaysInMonth = intArrayOf(31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 29)

    var jy = year
    var jm = month
    var jd = day

    jy += 1595
    var days = -355668 + 365 * jy + jy / 33 * 8 + (jy % 33 + 3) / 4
    for (i in 0 until jm - 1) {
        days += jDaysInMonth[i]
    }
    days += jd

    var gy = 400 * (days / 146097)
    days %= 146097
    if (days > 36524) {
        gy += 100 * (--days / 36524)
        days %= 36524
        if (days >= 365) days++
    }
    gy += 4 * (days / 1461)
    days %= 1461
    if (days > 365) {
        gy += (days - 1) / 365
        days = (days - 1) % 365
    }

    var gm = 0
    var gd = days + 2
    while (gd > gDaysInMonth[gm]) {
        gd -= gDaysInMonth[gm]
        if (gm == 1 && (gy % 4 == 0 && gy % 100 != 0 || gy % 400 == 0)) gd--
        gm++
    }
    return Triple(gy, gm + 1, gd)
}


fun gregorianToPersian(year: Int, month: Int, day: Int): Triple<Int, Int, Int> {
    val gDaysInMonth = intArrayOf(31,28,31,30,31,30,31,31,30,31,30,31)
    val jDaysInMonth = intArrayOf(31,31,31,31,31,31,30,30,30,30,30,29)

    var gy = year - 1600
    var gm = month - 1
    var gd = day - 1

    var gDayNo = 365*gy + gy/4 - gy/100 + gy/400
    for (i in 0 until gm) gDayNo += gDaysInMonth[i]
    if (gm > 1 && ((year%4==0 && year%100 !=0) || (year%400==0))) gDayNo += 1
    gDayNo += gd

    var jDayNo = gDayNo - 79

    val jNp = jDayNo / 12053
    jDayNo %= 12053

    var jy = 979 + 33*jNp + 4*(jDayNo/1461)
    jDayNo %= 1461

    if (jDayNo >= 366) {
        jy += (jDayNo-366)/365
        jDayNo = (jDayNo-366)%365
    }

    var jm = 0
    var jd = 0
    for (i in 0..11) {
        if (jDayNo < jDaysInMonth[i]) {
            jm = i+1
            jd = jDayNo+1
            break
        } else {
            jDayNo -= jDaysInMonth[i]
        }
    }
    return Triple(jy,jm,jd)
}

fun generateMonthDays(year: Int, month: Int): List<CalendarDay> {
    val days = mutableListOf<CalendarDay>()

    // ۱- پیدا کردن اولین روز ماه شمسی
    val (gy, gm, gd) = persianToGregorian(year, month, 1)
    val cal = Calendar.getInstance().apply {
        set(gy, gm - 1, gd)
    }
    val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) // 1=Sunday .. 7=Saturday
    val firstDayOfWeek = getWeekDayOfFirstDay(year, month)// شنبه=0 .. جمعه=6
    // ۲- روزهای ماه قبل (کم‌رنگ)
    val prevMonth = if (month == 1) 12 else month - 1
    val prevYear = if (month == 1) year - 1 else year
    val prevMonthDays = daysInMonth[prevMonth - 1]

    for (i in (prevMonthDays - firstDayOfWeek + 1)..prevMonthDays) {
        days.add(CalendarDay(i, month = prevMonth , year = prevYear, isCurrentMonth = false))
    }

    // ۳- روزهای ماه جاری
    val currentMonthDays = daysInMonth[month - 1]
    for (i in 1..currentMonthDays) {
        days.add(CalendarDay(i, month = month , year = year, isCurrentMonth = true))
    }

    // ۴- روزهای ماه بعد (برای پر شدن جدول ۶×۷)
    while (days.size % 7 != 0) {
        val nextDay = days.size - (currentMonthDays + firstDayOfWeek) + 1
        days.add(CalendarDay(nextDay, month = month , year = year, isCurrentMonth = false))
    }

    return days
}
private fun getWeekDayOfFirstDay(jYear: Int, jMonth: Int): Int {
    // یکم ماه رو به میلادی تبدیل می‌کنیم
    val (gy, gm, gd) = persianToGregorian(jYear, jMonth, 1)

    val cal = java.util.Calendar.getInstance()
    cal.set(gy, gm - 1, gd) // ماه میلادی از 0 شروع میشه

    val dayOfWeek = cal.get(java.util.Calendar.DAY_OF_WEEK) // SUNDAY=1 ... SATURDAY=7

    // حالا میاریمش روی مدل ایرانی: شنبه=0 ... جمعه=6
    return when (dayOfWeek) {
        java.util.Calendar.SATURDAY -> 6
        else -> dayOfWeek - 1
    }
}


enum class G2JTypes(value: String) {
    AGE("age"),
    YEAR("year"),
    MONTH("month"),
    DAY("day"),
    DISTANCECALC("distance_calc"),
    FULL("full"),
    DATE("dater"),
    MONTHINYEAR("month_in_year"),
    PERSIANMONTH("persian_month"),
    MONTHLENGTH("month_lenght"),
}