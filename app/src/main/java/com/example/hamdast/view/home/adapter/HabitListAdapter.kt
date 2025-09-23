package com.example.hamdast.view.home.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hamdast.data.models.habit.HabitModel
import com.example.hamdast.databinding.AdapterHabbitItemBinding
import com.example.hamdast.view.viewmodels.HabitViewModel

class HabitListAdapter(
    private val items: List<HabitModel>,
    private val activity: Activity,
    private val viewModel: HabitViewModel
) : RecyclerView.Adapter<HabitListAdapter.HabitItemHolder>() {

    inner class HabitItemHolder(private val binding: AdapterHabbitItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: HabitModel) {
            binding.apply {
                tvHabitTitle.text = item.title
                tvHabitDesc.text = item.desc
                tvRepeat.text = item.repeatType.name
                // اینجا می‌تونی با com.example.hamdast.data.models.habit.HabitLog چک کنی که امروز انجام شده یا نه
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitItemHolder {
        return HabitItemHolder(
            AdapterHabbitItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HabitItemHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}
