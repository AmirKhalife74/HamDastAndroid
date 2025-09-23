package com.example.hamdast.view.profile.adapter

import android.app.Activity
import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import com.example.hamdast.data.models.settingsItem.SettingsModel
import com.example.hamdast.data.models.task.TaskModel
import com.example.hamdast.databinding.AdapterTaskItemBinding
import com.example.hamdast.databinding.ItemSettingBinding
import com.example.hamdast.utils.areYouSureDialog
import com.example.hamdast.view.viewmodels.TaskViewModel


class SettingsAdapter(
    private val items: List<SettingsModel>,
    private val context: Context

) : Adapter<SettingsAdapter.SettingItemHolder>() {


    inner class SettingItemHolder(private var binding: ItemSettingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SettingsModel) {
            binding.apply {
                tvTitle.text = item.title
                tvSubtitle.text = item.subTitle
                imgSetting.setImageResource(item.imgSrc)

                cardMain.setOnClickListener {
                    item.onClick()
                }


            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingItemHolder {
        val viewHolder = SettingItemHolder(
            ItemSettingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        viewHolder.setIsRecyclable(false);
        return viewHolder
    }

    override fun onBindViewHolder(holder: SettingItemHolder, position: Int) {
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