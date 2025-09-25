package com.example.hamdast.view.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hamdast.data.models.calendar.CalendarDay
import com.example.hamdast.databinding.ItemWeekBinding

class WeeklyAdapter(
    private val weeks: List<List<CalendarDay>>,
    private val context: Context
) : RecyclerView.Adapter<WeeklyAdapter.WeekViewHolder>() {

    inner class WeekViewHolder(private val binding: ItemWeekBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(week: List<CalendarDay>) {
            // تنظیم RecyclerView داخلی برای نمایش روزهای هفته
            val daysAdapter = WeeklyDaysAdapter(
                items = week,
                context = context
            ){selectedDay ->

            }
            binding.rcDays.layoutManager = GridLayoutManager(context, 7)
            binding.rcDays.adapter = daysAdapter

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
        weeks.toMutableList().addAll(newWeeks)
        notifyDataSetChanged()
    }

    fun addWeeksBefore(newWeeks: List<List<CalendarDay>>) {
        weeks.toMutableList().addAll(0, newWeeks)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: WeekViewHolder, position: Int) {
        holder.bind(weeks[position])
    }

    override fun getItemCount(): Int = weeks.size
}