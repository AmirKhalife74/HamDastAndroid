package com.example.hamdast.view.home.adapter

import android.app.Activity
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import com.example.hamdast.data.models.TaskModel
import com.example.hamdast.databinding.AdapterTaskItemBinding
import com.example.hamdast.utils.areYouSureDialog
import com.example.hamdast.view.viewmodels.TaskViewModel


class TaskListAdapter(
    private val items: List<TaskModel>,
    private val activity: Activity,
    private val viewModel: TaskViewModel

) : Adapter<TaskListAdapter.TaskItemHolder>() {


    inner class TaskItemHolder(private var binding: AdapterTaskItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TaskModel) {
            binding.apply {
                tvTaskTitle.text = item.title
                tvDesc.text = item.desc
                tvDate.text = item.date

                radioIsDone.isChecked = item.isDone

                radioIsDone.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (isChecked) {
                       activity.areYouSureDialog(title = "انجام شد ؟",desc = "آیا از انجام شدن تسک مطمین هستید ؟") { res ->
                           if (res == true){
                               viewModel.updateTaskDone(item.id,true)
                           }else
                           {
                               radioIsDone.isChecked = false
                           }
                       }
                    }
                }
            }
        }


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskItemHolder {
        val viewHolder = TaskItemHolder(
            AdapterTaskItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        viewHolder.setIsRecyclable(false);
        return viewHolder
    }

    override fun onBindViewHolder(holder: TaskItemHolder, position: Int) {
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