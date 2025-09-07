package com.example.hamdast.view.home.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import com.example.hamdast.R
import com.example.hamdast.data.models.CalendarDay
import com.example.hamdast.databinding.ItemWeeklyDayBinding
import com.example.hamdast.utils.getTodayPersianDateParts


class WeeklyDaysAdapter(
    private val items: List<CalendarDay>,
    private val today: Triple<Int, Int, Int> = getTodayPersianDateParts(),
    private val context: Context
) : Adapter<WeeklyDaysAdapter.WeeklyViewHolder>() {


    inner class WeeklyViewHolder(private var binding: ItemWeeklyDayBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CalendarDay) {
            binding.apply {
                if (today.first == item.year && today.second == item.month && today.third == item.day){
                    crdItem.setCardBackgroundColor(context.resources.getColor( R.color.light_primary_variant))
                }
              tvProgressDone.text = item.day.toString()
                item.percentageTaskHasBeenDone?.let {
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
        viewHolder.setIsRecyclable(false);
        return viewHolder
    }

    override fun onBindViewHolder(holder: WeeklyViewHolder, position: Int) {
        holder.bind(items[position])

    }

    override fun getItemCount(): Int {
        return items.size
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


}