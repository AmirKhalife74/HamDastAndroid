package com.example.hamdast.utils

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import androidx.transition.Visibility
import com.example.hamdast.R
import com.example.hamdast.data.models.HabitModel
import com.example.hamdast.data.models.RepeatType
import com.example.hamdast.data.models.TaskCategory
import com.example.hamdast.data.models.TaskModel
import com.example.hamdast.databinding.BottomsheetAddHabitBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog
import ir.hamsaa.persiandatepicker.api.PersianPickerDate
import ir.hamsaa.persiandatepicker.api.PersianPickerListener
import kotlin.Int

class HabitBottomSheetDialog(
    private val onHabitCreated: (HabitModel?, TaskModel?) -> Unit?
) : BottomSheetDialogFragment() {
    private var isTask: Boolean = true
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

        init()
        listeners()


    }

    private fun init(){
        setCategoryList()
        setButtons()
    }

    private fun setButtons()
    {
        binding.apply {
            btnTasks.setOnClickListener {
                isTask = true
                TransitionManager.beginDelayedTransition(binding.root as ViewGroup, AutoTransition())
                binding.lytAddNewTask.visibility = View.VISIBLE
                binding.lytAddNewHabits.visibility = View.GONE
                btnHabits.setBackgroundColor(requireContext().resources.getColor(R.color.light_primary_variant))
                btnHabits.setTextColor(requireContext().resources.getColor(R.color.light_text_primary))
                btnTasks.setBackgroundColor(requireContext().resources.getColor(R.color.light_primary))
                btnTasks.setTextColor(requireContext().resources.getColor(R.color.dark_text_primary))
            }

            btnHabits.setOnClickListener {
                isTask = false
                TransitionManager.beginDelayedTransition(binding.root as ViewGroup, AutoTransition())
                binding.lytAddNewHabits.visibility = View.VISIBLE
                binding.lytAddNewTask.visibility = View.GONE
                btnTasks.setBackgroundColor(requireContext().resources.getColor(R.color.light_primary_variant))
                btnTasks.setTextColor(requireContext().resources.getColor(R.color.light_text_primary))
                btnHabits.setBackgroundColor(requireContext().resources.getColor(R.color.light_primary))
                btnHabits.setTextColor(requireContext().resources.getColor(R.color.dark_text_primary))
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun listeners()
    {
        binding.apply {

            tvSelectedDate.setOnClickListener {
                setDatePicker()
            }

            btnSaveHabit.setOnClickListener {
                val categories = TaskCategory.entries.toTypedArray()
                if (isTask){

                    val title =edtTaskName.text.toString()
                    val desc = edtTaskDesc.text.toString()
                    val time = tvSelectedTime.text.toString()
                    val hour = time.split(":")[0]
                    val minute = time.split(":")[1]

                    val dateStr = tvSelectedDate.text.toString()
                    val hourInt = hour.toInt()
                    val minuteInt = minute.toInt()
                    println("Input Date: $dateStr, Hour: $hourInt, Minute: $minuteInt")
                    val date = persianDateTimeToMillis(dateStr, hourInt, minuteInt)
                    val year = tvSelectedDate.text.toString().split("/")[0]
                    val month = tvSelectedDate.text.toString().split("/")[1]
                    val day = tvSelectedDate.text.toString().split("/")[2]
                    var selectedCategory = categories[0]
                    categoryDropdown.setOnItemClickListener { parent, view, position, id ->
                        selectedCategory = categories[position]

                    }

                    val taskModel = TaskModel(title = title, desc = desc, isDone = false, year = year.toInt(), month = month.toInt(), day = day.toInt(), time= time, timeInTimeStamp = date, category = selectedCategory)
                    onHabitCreated(null,taskModel)
                    dismiss()

                }else {
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
                    val habit = HabitModel(
                        title = title,
                        desc = desc,
                        repeatType = repeatType,
                        daysOfWeek = emptyList(), // TODO: انتخاب روزها
                        dayOfMonth = null,
                        monthOfYear = null,
                        dayOfYear = null
                    )

                    onHabitCreated(habit,null)
                    dismiss()
                }

            }

            tvSelectedTime.setOnClickListener {
                setTimePicker()
            }

        }



    }

    private fun setDatePicker()
    {
        binding.apply {
            PersianDatePickerDialog(requireContext())
                .setPositiveButtonString("تأیید")
                .setNegativeButton("بیخیال")
                .setTodayButton("امروز")
                .setTodayButtonVisible(true)

                .setMaxYear(PersianDatePickerDialog.THIS_YEAR)
                .setMinYear(1300)
                .setListener(object : PersianPickerListener {
                    @SuppressLint("DefaultLocale")
                    override fun onDateSelected(persianPickerDate: PersianPickerDate) {
                        val formattedDate = String.format("%04d/%02d/%02d",
                            persianPickerDate.persianYear,
                            persianPickerDate.persianMonth,
                            persianPickerDate.persianDay
                        )
                        Log.d("DATE", formattedDate)
                        tvSelectedDate.text = formattedDate
                    }

                    override fun onDismissed() {
                        // کاربر بست
                    }
                }).show()
        }
    }

    @SuppressLint("DefaultLocale")
    private fun setTimePicker()
    {
        binding.apply {
            val picker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("ساعت را انتخاب کن")
                .build()

            picker.show(parentFragmentManager,"")

            picker.addOnPositiveButtonClickListener {
                val timeString = String.format("%02d:%02d", picker.hour, picker.minute)
                tvSelectedTime.text = timeString
            }
        }
    }

    private fun setCategoryList(){
        binding.apply {
            val categories = TaskCategory.entries.map { "${it.emoji} ${it.displayName}" }
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                categories
            )

            categoryDropdown.setAdapter(adapter)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

