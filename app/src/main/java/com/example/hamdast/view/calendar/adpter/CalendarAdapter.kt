package com.example.hamdast.view.calendar.adpter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hamdast.R
import com.example.hamdast.data.models.calendar.CalendarDay
import com.example.hamdast.data.models.task.TaskModel
import com.example.hamdast.databinding.ItemDayBinding
import com.example.hamdast.utils.gregorianToPersian
import android.view.animation.DecelerateInterpolator
import androidx.transition.Visibility
import java.util.Calendar

class CalendarAdapter(
    private var days: List<CalendarDay>,
    private val tasksByDay: List<TaskModel>,
    private val selectedYear: Int,
    private val selectedMonth: Int,
    private val recyclerView: RecyclerView,
    private val onDayClick: (Int) -> Unit
) : RecyclerView.Adapter<CalendarAdapter.DayViewHolder>() {

    private var selectedDay: Int? = null

    private val jToday: Triple<Int, Int, Int> by lazy {
        val cal = Calendar.getInstance()
        gregorianToPersian(
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH) + 1,
            cal.get(Calendar.DAY_OF_MONTH)
        )
    }

    fun updateSelectedDay(day: Int) {
        val previousPosition = selectedDay?.let { days.indexOfFirst { it.day == selectedDay && it.isCurrentMonth } } ?: -1
        selectedDay = day
        val newPosition = days.indexOfFirst { it.day == day && it.isCurrentMonth }
        if (previousPosition != -1) notifyItemChanged(previousPosition)
        if (newPosition != -1) notifyItemChanged(newPosition)
        animateCardView(previousPosition, newPosition)
    }

    @SuppressLint("ResourceAsColor")
    private fun animateCardView(previousPosition: Int, newPosition: Int) {
        val previousHolder = if (previousPosition != -1) {
            recyclerView.findViewHolderForAdapterPosition(previousPosition) as? DayViewHolder
        } else null
        val newHolder = if (newPosition != -1) {
            recyclerView.findViewHolderForAdapterPosition(newPosition) as? DayViewHolder
        } else null

        // ریست ویوی قبلی با انیمیشن محو شدن ملایم
        previousHolder?.binding?.apply {
            dayParent.animate()
                .alpha(0f) // محو شدن
                .setDuration(400)
                .setInterpolator(DecelerateInterpolator())
                .withEndAction {
                    tvDay.setBackgroundColor(Color.TRANSPARENT)
                    tvDay.setTextColor(Color.BLACK)
                    dayParent.cardElevation = 0f
                    dayParent.alpha = 1f // بازگرداندن شفافیت
                }
                .start()
        }

        // انیمیشن برای ویوی جدید با جابجایی و ظاهر شدن ملایم
        newHolder?.binding?.apply {
            dayParent.cardElevation = 8f
            tvDay.setBackgroundColor(R.color.light_primary)
            tvDay.setTextColor(Color.WHITE)

            // محاسبه فاصله برای جابجایی
            val deltaX = if (previousHolder != null) {
                newHolder.binding.dayParent.x - previousHolder.binding.dayParent.x
            } else 0f
            val deltaY = if (previousHolder != null) {
                newHolder.binding.dayParent.y - previousHolder.binding.dayParent.y
            } else 0f

            // تنظیم موقعیت اولیه برای انیمیشن
            dayParent.translationX = deltaX
            dayParent.translationY = deltaY
            dayParent.alpha = 0f

            // انیمیشن جابجایی و ظاهر شدن
            dayParent.animate()
                .translationX(0f)
                .translationY(0f)
                .alpha(1f)
                .setDuration(400)
                .setInterpolator(DecelerateInterpolator())
                .start()
        }
    }

    inner class DayViewHolder(val binding: ItemDayBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ResourceAsColor")
        fun bind(item: CalendarDay) {
            binding.apply {
                tvDay.text = item.day.toString()

                dayParent.cardElevation = 0f
                dayParent.alpha = 1f

                if (!item.isCurrentMonth) {
                    tvDay.setTextColor(Color.GRAY)
                    tvDay.setOnClickListener(null)
                } else {
                    tvDay.setTextColor(Color.BLACK)
                    if (item.day == selectedDay) {
                        tvDay.setBackgroundColor(R.color.light_primary)
                        tvDay.setTextColor(Color.WHITE)
                        dayParent.cardElevation = 8f
                    }else{
                        tvDay.setBackgroundColor(Color.TRANSPARENT)
                        tvDay.setTextColor(Color.BLACK)
                        dayParent.cardElevation = 0f
                    }

                    tasksByDay.forEach { taskByDay ->
                        if (taskByDay.day == item.day) {
                            hasTask.visibility = View.VISIBLE
                        }
                    }

                    tvDay.setOnClickListener {
                        if (selectedDay != item.day) {
                            updateSelectedDay(item.day)
                            onDayClick(item.day)
                        }
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        return DayViewHolder(
            ItemDayBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        holder.bind(days[position])
    }

    override fun getItemCount(): Int = days.size
}