package com.example.hamdast.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class HabitModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,
    val desc: String,

    // وضعیت انجام شدن در یک روز مشخص
    var isDone: Boolean = false,

    // نوع تکرار
    val repeatType: RepeatType,

    // برای تکرارهای خاص
    val repeatInterval: Int = 1,     // مثلا هر 2 روز یکبار، یا هر 3 هفته یکبار

    // اگر هفتگی باشه → لیست روزهای هفته (0 = شنبه .. 6 = جمعه)
    val daysOfWeek: List<Int>?,

    // اگر ماهانه باشه → روز مشخص ماه
    val dayOfMonth: Int?,

    // اگر سالانه باشه → ماه و روز مشخص
    val monthOfYear: Int?,
    val dayOfYear: Int?,

    // تعداد دفعات در هر روز
    val timesPerDay: Int = 1
)

enum class RepeatType {
    DAILY,      // هر روز
    WEEKLY,     // روزهای خاص هفته
    MONTHLY,    // یک روز خاص هر ماه
    YEARLY,     // یک روز خاص هر سال
    CUSTOM      // الگوهای خاص مثل روزهای زوج، یا هر 3 روز یک بار
}
