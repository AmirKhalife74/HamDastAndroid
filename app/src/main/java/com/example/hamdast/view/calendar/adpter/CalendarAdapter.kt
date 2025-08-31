package com.example.hamdast.view.calendar.adpter

import android.graphics.Color
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hamdast.R
import com.example.hamdast.data.models.CalendarDay
import com.example.hamdast.utils.gregorianToPersian
import java.util.Calendar

class CalendarAdapter(
    private var days: List<CalendarDay>,
    private val tasksByDay: Set<Int>,
    private val selectedYear: Int,
    private val selectedMonth: Int,
    private val onDayClick: (Int) -> Unit
) : RecyclerView.Adapter<CalendarAdapter.DayViewHolder>() {

    private val jToday: Triple<Int, Int, Int> by lazy {
        val cal = Calendar.getInstance()
        gregorianToPersian(
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH) + 1,
            cal.get(Calendar.DAY_OF_MONTH)
        )
    }

    inner class DayViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val tv = TextView(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                120
            )
            textAlignment = TextView.TEXT_ALIGNMENT_CENTER
            textSize = 16f
            setPadding(0, 16, 0, 16)
        }
        return DayViewHolder(tv)
    }

    override fun getItemCount(): Int = days.size

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val item = days[position]
        val tv = holder.textView

        tv.text = item.day.toString()
        tv.setBackgroundColor(Color.TRANSPARENT)

        if (!item.isCurrentMonth) {
            // روزهای ماه قبل/بعد → کم‌رنگ و غیرفعال
            tv.setTextColor(Color.GRAY)
            tv.setOnClickListener(null)
        } else {
            // روزهای ماه جاری
            tv.setTextColor(Color.BLACK)
            tv.setOnClickListener { onDayClick(item.day) }

            if (tasksByDay.contains(item.day)) {
                tv.setBackgroundResource(R.drawable.bg_day_has_task)
            }

            if (selectedYear == jToday.first &&
                selectedMonth == jToday.second &&
                item.day == jToday.third) {
                tv.setBackgroundResource(R.drawable.bg_today)
                tv.setTextColor(Color.WHITE)
            }
        }
    }
}
