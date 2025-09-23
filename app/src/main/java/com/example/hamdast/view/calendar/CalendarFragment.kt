package com.example.hamdast.view.calendar

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hamdast.data.models.calendar.CalendarDay
import com.example.hamdast.data.models.habit.HabitModel
import com.example.hamdast.data.models.task.TaskModel
import com.example.hamdast.databinding.FragmentCalendarBinding
import com.example.hamdast.utils.generateMonthDays
import com.example.hamdast.utils.gregorianToPersian
import com.example.hamdast.utils.persianToGregorian
import com.example.hamdast.R
import com.example.hamdast.utils.persionMonth
import com.example.hamdast.utils.shouldShowOn
import com.example.hamdast.view.calendar.adpter.CalendarAdapter
import com.example.hamdast.view.home.adapter.HabitListAdapter
import com.example.hamdast.view.home.adapter.TaskListAdapter
import com.example.hamdast.view.viewmodels.HabitViewModel
import com.example.hamdast.view.viewmodels.TaskViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CalendarFragment : Fragment() {

    private lateinit var binding: FragmentCalendarBinding
    private val viewModel: TaskViewModel by viewModels()
    private val habitViewModel: HabitViewModel by viewModels()
    private var calendarAdapter: CalendarAdapter? = null

    private var selectedYear = 1404
    private var selectedMonth = 6

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCalendar()
        init()
        binding.btnPrev.setOnClickListener { moveToPrevMonth() }
        binding.btnNext.setOnClickListener { moveToNextMonth() }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n", "SuspiciousIndentation")
    private fun setupCalendar() {
        lifecycleScope.launch {
            viewModel.getTasksInMonth(selectedYear, selectedMonth).collect { tasks ->
                var daysWithTasks: List<TaskModel> = listOf()
                tasks.mapNotNull { task ->
                    val (jy, jm, jd) = gregorianToPersian(
                        task.year,
                        task.month,
                        task.day
                    )
                    if (jy == selectedYear && jm == selectedMonth) jd else null
                    daysWithTasks = tasks
                }.toSet()
                var habitsForDay = emptyList<HabitModel>()
                val daysList = generateMonthDays(selectedYear, selectedMonth, tasks)
                calendarAdapter = CalendarAdapter(
                    days = daysList,
                    tasksByDay = daysWithTasks,
                    selectedYear = selectedYear,
                    selectedMonth = selectedMonth,
                    recyclerView = binding.rcCalendar, // پاس دادن RecyclerView
                    onDayClick = { day -> onDayClicked(day, tasks, habitsForDay) }
                )

                binding.rcCalendar.layoutManager = GridLayoutManager(requireContext(), 7)
                binding.rcCalendar.adapter = calendarAdapter
            }
        }

        binding.tvMonth.text = "${persionMonth[selectedMonth - 1]} $selectedYear"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun onDayClicked(day: Int, tasks: List<TaskModel>, habits: List<HabitModel>) {
        calendarAdapter?.updateSelectedDay(day) // آپدیت روز انتخاب‌شده
        var tasksOfDays = mutableListOf<TaskModel>()
        tasks.forEach { task ->
            if (task.day == day) {
                tasksOfDays.add(task)
            }
        }
        val habitsOfDay = habits.filter { it.shouldShowOn(CalendarDay(day, selectedMonth, selectedYear, true)) }
        showTasks(tasksOfDays)
        showHabits(habitsOfDay)
    }

    private fun showTasks(tasks: List<TaskModel>) {
        val adapter = TaskListAdapter(tasks, requireActivity(), viewModel)
        binding.rcDayTasks.layoutManager = LinearLayoutManager(requireContext())
        binding.rcDayTasks.adapter = adapter
    }

    private fun showHabits(habits: List<HabitModel>) {
        val adapter = HabitListAdapter(
            habits,
            activity = requireActivity(),
            viewModel = habitViewModel
        )
        // binding.rcDayHabit.layoutManager = LinearLayoutManager(requireContext())
        // binding.rcDayHabit.adapter = adapter
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun moveToPrevMonth() {
        if (selectedMonth == 1) {
            selectedMonth = 12
            selectedYear--
        } else {
            selectedMonth--
        }
        setupCalendar()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun moveToNextMonth() {
        if (selectedMonth == 12) {
            selectedMonth = 1
            selectedYear++
        } else {
            selectedMonth++
        }
        setupCalendar()
    }

    private fun init()
    {
        binding.apply {
            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.peekHeight = resources.getDimensionPixelSize(R.dimen.calendar_height)
        }
    }
}