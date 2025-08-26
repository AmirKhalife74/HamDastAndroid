package com.example.hamdast.utils

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment

fun Fragment.addNewTask(
    items: List<String>,
) {
    view?.let {
        val binding = DialogCallSupportBinding.inflate(LayoutInflater.from(activity!!))
        val dialog = Dialog(activity!!)
        dialog.setContentView(binding.root)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        dialog.window!!.setGravity(Gravity.BOTTOM)

        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)

        binding.apply {
            items?.let {

            }

        }

        dialog.show()

    }
}