package com.example.hamdast.view.calendar

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hamdast.data.models.TaskModel
import com.example.hamdast.databinding.FragmentCalendarBinding
import com.example.hamdast.utils.generateMonthDays
import com.example.hamdast.utils.gregorianToPersian
import com.example.hamdast.utils.persianToGregorian
import com.example.hamdast.utils.persionMonth
import com.example.hamdast.view.calendar.adpter.CalendarAdapter
import com.example.hamdast.view.home.adapter.TaskListAdapter
import com.example.hamdast.view.viewmodels.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CalendarFragment : Fragment() {

    private lateinit var binding: FragmentCalendarBinding
    private val viewModel: TaskViewModel by viewModels()
    private var calendarAdapter: CalendarAdapter? = null

    private var selectedYear = 1404
    private var selectedMonth = 8

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCalendar()

        binding.btnPrev.setOnClickListener { moveToPrevMonth() }
        binding.btnNext.setOnClickListener { moveToNextMonth() }
    }

    @SuppressLint("SetTextI18n")
    private fun setupCalendar() {


        lifecycleScope.launch {
            viewModel.getTasksInMonth(selectedYear, selectedMonth).collect { tasks ->
                val daysWithTasks = tasks.mapNotNull { task ->
                    val (jy, jm, jd) = gregorianToPersian(
                        task.year,
                        task.month,
                        task.day
                    )
                    if (jy == selectedYear && jm == selectedMonth) jd else null
                }.toSet()
                val daysList = generateMonthDays(selectedYear, selectedMonth,tasks)
                calendarAdapter = CalendarAdapter(
                    days = daysList,
                    tasksByDay = daysWithTasks,
                    selectedYear = selectedYear,
                    selectedMonth = selectedMonth,
                    onDayClick = { day -> onDayClicked(day,tasks) }
                )

                binding.rcCalendar.layoutManager = GridLayoutManager(requireContext(), 7)
                binding.rcCalendar.adapter = calendarAdapter
            }
        }

        binding.tvMonth.text = "${persionMonth[selectedMonth - 1]} $selectedYear"
    }


    private fun onDayClicked(day: Int,tasks:List<TaskModel>) {
        var tasksOfDays = mutableListOf<TaskModel>()
        tasks.forEach { task ->
            if (task.day == day){
                tasksOfDays.add(task)
            }
        }
        val (gy, gm, gd) = persianToGregorian(selectedYear, selectedMonth, day)
        showTasks(tasksOfDays)
    }

    private fun showTasks(tasks: List<TaskModel>) {

        val adapter = TaskListAdapter(tasks, requireActivity(), viewModel)
        binding.rcDayTasks.layoutManager = LinearLayoutManager(requireContext())
        binding.rcDayTasks.adapter = adapter
    }

    private fun moveToPrevMonth() {
        if (selectedMonth == 1) {
            selectedMonth = 12
            selectedYear--
        } else {
            selectedMonth--
        }
        setupCalendar()
    }

    private fun moveToNextMonth() {
        if (selectedMonth == 12) {
            selectedMonth = 1
            selectedYear++
        } else {
            selectedMonth++
        }
        setupCalendar()
    }
}
