package com.example.hamdast.view.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hamdast.data.models.calendar.CalendarDay
import com.example.hamdast.databinding.ItemWeekBinding
import com.example.hamdast.utils.persionMonth

class WeeklyAdapter(
    private val weeks: List<List<CalendarDay>>,
    private val context: Context
) : RecyclerView.Adapter<WeeklyAdapter.WeekViewHolder>() {

    private var selectedWeek: List<CalendarDay>? = null
    private var selectedMonth: String = ""
    private var selectedYear: String = ""
    private var onMonthYearChanged: ((String, String) -> Unit)? = null
    private var currentPosition: Int = -1 // موقعیت هفته فعلی

    inner class WeekViewHolder(private val binding: ItemWeekBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(week: List<CalendarDay>, position: Int) {
            selectedWeek = week
            val daysAdapter = WeeklyDaysAdapter(
                items = week,
                context = context
            ) { selectedDay ->
                // منطق برای انتخاب روز
            }

            binding.rcDays.layoutManager = GridLayoutManager(context, 7)
            binding.rcDays.adapter = daysAdapter

            // فقط اگر این هفته، هفته فعلی است، مقادیر ماه و سال رو به‌روزرسانی کن
            if (position == currentPosition) {
                selectedMonth = daysAdapter.getMonth()
                selectedYear = daysAdapter.getYear()
                notifyMonthYearChanged()
            }
        }
    }

    fun setOnMonthYearChangedListener(listener: (String, String) -> Unit) {
        onMonthYearChanged = listener
    }

    private fun notifyMonthYearChanged() {
        onMonthYearChanged?.invoke(selectedMonth, selectedYear)
    }

    // متد جدید برای تنظیم هفته فعلی
    fun setCurrentWeek(position: Int) {
        if (position in 0 until weeks.size) {
            currentPosition = position
            selectedWeek = weeks[position]
            val daysAdapter = WeeklyDaysAdapter(items = selectedWeek!!, context = context) { }
            selectedMonth = daysAdapter.getMonth()
            selectedYear = daysAdapter.getYear()
            notifyMonthYearChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekViewHolder {
        val binding = ItemWeekBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return WeekViewHolder(binding)
    }

    fun addWeeks(newWeeks: List<List<CalendarDay>>) {
        val startPosition = weeks.size
        weeks.toMutableList().addAll(newWeeks)
        notifyItemRangeInserted(startPosition, newWeeks.size)
    }

    fun addWeeksBefore(newWeeks: List<List<CalendarDay>>) {
        weeks.toMutableList().addAll(0, newWeeks)
        notifyItemRangeInserted(0, newWeeks.size)
    }

    fun getMonth(): String = selectedMonth

    fun getYear(): String = selectedYear

    override fun onBindViewHolder(holder: WeekViewHolder, position: Int) {
        holder.bind(weeks[position], position)
    }

    override fun getItemCount(): Int = weeks.size
}