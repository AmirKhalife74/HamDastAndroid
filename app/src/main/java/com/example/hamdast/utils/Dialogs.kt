package com.example.hamdast.utils

import android.app.Dialog
import android.graphics.Color

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.hamdast.databinding.DialogAddNewTaskBinding
import androidx.core.graphics.drawable.toDrawable
import com.example.hamdast.data.models.TaskModel
import com.example.hamdast.view.viewmodels.TaskViewModel

fun Fragment.addNewTask(
    viewModel: TaskViewModel
) {
    view?.let {
        val binding = DialogAddNewTaskBinding.inflate(LayoutInflater.from(activity!!))
        val dialog = Dialog(activity!!)
        dialog.setContentView(binding.root)
        dialog.window!!.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setGravity(Gravity.BOTTOM)

        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)

        binding.apply {

                btnAddNewTask.setOnClickListener {
                    var task = TaskModel(
                        title = edtNewTaskTitle.text.toString(),
                        desc = edtDesc.text.toString(),
                        date = ""
                    )
                    viewModel.addTask(task)
                    dialog.dismiss()
                }

        }

        dialog.show()

    }
}