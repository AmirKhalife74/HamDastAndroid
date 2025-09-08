package com.example.hamdast.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hamdast.data.models.RepeatType
import com.example.hamdast.databinding.BottomsheetAddHabitBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class HabitBottomSheetDialog(
    private val onHabitCreated: (HabitData) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: BottomsheetAddHabitBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomsheetAddHabitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSaveHabit.setOnClickListener {
            val title = binding.etHabitTitle.text.toString()
            val desc = binding.etHabitDesc.text.toString()
            val repeatType = when (binding.spinnerRepeatType.selectedItemPosition) {
                0 -> RepeatType.DAILY
                1 -> RepeatType.WEEKLY
                2 -> RepeatType.MONTHLY
                3 -> RepeatType.YEARLY
                else -> RepeatType.CUSTOM
            }

            // دیتا ساده → بعداً مپ می‌کنیم به HabitModel
            val habit = HabitData(
                title = title,
                desc = desc,
                repeatType = repeatType,
                daysOfWeek = emptyList(), // TODO: انتخاب روزها
                dayOfMonth = null,
                monthOfYear = null,
                dayOfYear = null
            )

            onHabitCreated(habit)
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

data class HabitData(
    val title: String,
    val desc: String,
    val repeatType: RepeatType,
    val daysOfWeek: List<Int>?,
    val dayOfMonth: Int?,
    val monthOfYear: Int?,
    val dayOfYear: Int?
)