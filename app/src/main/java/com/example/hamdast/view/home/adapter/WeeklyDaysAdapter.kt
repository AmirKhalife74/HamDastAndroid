package com.example.hamdast.view.home.adapter

import android.app.Activity
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import com.example.hamdast.data.models.CalendarDay
import com.example.hamdast.data.models.TaskModel
import com.example.hamdast.databinding.AdapterTaskItemBinding
import com.example.hamdast.databinding.ItemWeeklyDayBinding
import com.example.hamdast.utils.areYouSureDialog
import com.example.hamdast.view.viewmodels.TaskViewModel


class WeeklyDaysAdapter(
    private val items: List<CalendarDay>,
) : Adapter<WeeklyDaysAdapter.WeeklyViewHolder>() {


    inner class WeeklyViewHolder(private var binding: ItemWeeklyDayBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CalendarDay) {
            binding.apply {
              tvProgressDone.text = item.percentageTaskHasBeenDone.toString()
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