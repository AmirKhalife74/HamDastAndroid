package com.example.hamdast.view.home.adapter

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.hamdast.R
import com.example.hamdast.data.models.calendar.CalendarDay
import com.example.hamdast.databinding.ItemWeeklyDayBinding
import com.example.hamdast.utils.daysOfWeekInPersian
import com.example.hamdast.utils.getTodayPersianDateParts

class WeeklyDaysAdapter(
    private val items: List<CalendarDay>,
    private val today: Triple<Int, Int, Int> = getTodayPersianDateParts(),
    private val context: Context,
    private var onDayClicked: (selectedDay: CalendarDay) -> Unit
) : RecyclerView.Adapter<WeeklyDaysAdapter.WeeklyViewHolder>() {

    private var selectedDay: CalendarDay? = null
    private var lastSelectedPosition: Int = -1

    inner class WeeklyViewHolder(private val binding: ItemWeeklyDayBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("NotifyDataSetChanged")
        fun bind(item: CalendarDay, position: Int) {
            binding.apply {
                tvNameOfDay.text = daysOfWeekInPersian[position]
                val isToday = today.first == item.year && today.second == item.month && today.third == item.day
                val isSelected = selectedDay == item

                // تنظیم رنگ اولیه
                updateViewColors(isSelected, isToday, animate = false)

                // کلیک روی آیتم
                crdItem.setOnClickListener {
                    val previousSelectedPosition = lastSelectedPosition
                    selectedDay = item
                    lastSelectedPosition = position
                    onDayClicked(item)

                    // به‌روزرسانی آیتم‌های تغییرکرده با انیمیشن
                    if (previousSelectedPosition != -1 && previousSelectedPosition != position) {
                        updateViewColors(isSelected, isToday, animate = true)
                        notifyItemChanged(previousSelectedPosition)
                    }
                    notifyItemChanged(position)
                }
            }
        }

        private fun updateViewColors(isSelected: Boolean, isToday: Boolean, animate: Boolean) {
            binding.apply {
                val backgroundColorStart = crdItem.cardBackgroundColor.defaultColor
                val backgroundColorEnd = when {
                    isSelected -> ContextCompat.getColor(context, R.color.light_primary)
                    isToday -> ContextCompat.getColor(context, R.color.light_primary_variant) // رنگ متفاوت برای روز جاری
                    else -> Color.WHITE
                }
                val textColorStart = tvProgressDone.currentTextColor
                val textColorEnd = when {
                    isSelected || isToday -> Color.WHITE
                    else -> ContextCompat.getColor(context, R.color.light_primary)
                }
                val nameTextColorEnd = when {
                    isSelected || isToday -> Color.WHITE
                    else -> Color.BLACK
                }

                if (animate) {
                    // انیمیشن تغییر رنگ برای پس‌زمینه
                    val backgroundAnimator = ValueAnimator.ofObject(ArgbEvaluator(), backgroundColorStart, backgroundColorEnd)
                    backgroundAnimator.duration = 300
                    backgroundAnimator.addUpdateListener { animator ->
                        crdItem.setCardBackgroundColor(animator.animatedValue as Int)
                    }

                    // انیمیشن تغییر رنگ برای متن‌ها
                    val textAnimator = ValueAnimator.ofObject(ArgbEvaluator(), textColorStart, textColorEnd)
                    textAnimator.duration = 300
                    textAnimator.addUpdateListener { animator ->
                        tvProgressDone.setTextColor(animator.animatedValue as Int)
                    }

                    val nameTextAnimator = ValueAnimator.ofObject(ArgbEvaluator(), tvNameOfDay.currentTextColor, nameTextColorEnd)
                    nameTextAnimator.duration = 300
                    nameTextAnimator.addUpdateListener { animator ->
                        tvNameOfDay.setTextColor(animator.animatedValue as Int)
                    }

                    backgroundAnimator.start()
                    textAnimator.start()
                    nameTextAnimator.start()
                } else {
                    // بدون انیمیشن
                    crdItem.setCardBackgroundColor(backgroundColorEnd)
                    tvProgressDone.setTextColor(textColorEnd)
                    tvNameOfDay.setTextColor(nameTextColorEnd)
                }

                // تنظیم متن و پیشرفت
                tvProgressDone.text = items[adapterPosition].day.toString()
                items[adapterPosition].percentageTaskHasBeenDone?.let {
                    prgrsTaskDone.progress = it
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeeklyViewHolder {
        val viewHolder = WeeklyViewHolder(
            ItemWeeklyDayBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        viewHolder.setIsRecyclable(false)
        return viewHolder
    }

    override fun onBindViewHolder(holder: WeeklyViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount(): Int = items.size

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemViewType(position: Int): Int = position
}