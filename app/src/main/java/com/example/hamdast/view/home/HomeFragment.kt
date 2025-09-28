package com.example.hamdast.view.home

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Visibility
import androidx.viewpager2.widget.ViewPager2
import com.example.hamdast.R
import com.example.hamdast.data.models.calendar.CalendarDay
import com.example.hamdast.data.models.habit.HabitModel
import com.example.hamdast.data.models.task.TaskModel
import com.example.hamdast.databinding.FragmentHomeBinding
import com.example.hamdast.utils.HabitBottomSheetDialog
import com.example.hamdast.utils.generateMonthDays
import com.example.hamdast.utils.getWeeksRange
import com.example.hamdast.utils.gregorianToPersian
import com.example.hamdast.utils.notifications.TaskScheduler
import com.example.hamdast.utils.persionMonth
import com.example.hamdast.utils.shouldShowOn
import com.example.hamdast.view.calendar.adpter.CalendarAdapter
import com.example.hamdast.view.home.adapter.HabitListAdapter
import com.example.hamdast.view.home.adapter.TaskListAdapter
import com.example.hamdast.view.home.adapter.WeeklyAdapter
import com.example.hamdast.view.viewmodels.HabitViewModel
import com.example.hamdast.view.viewmodels.TaskViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: TaskViewModel by viewModels()
    private val hobitViewModel: HabitViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    private var items: List<TaskModel>? = null
    private val taskViewModel: TaskViewModel by viewModels()
    private val habitViewModel: HabitViewModel by viewModels()
    private var selectedCalendarType: String = "هفتگی"

    private var calendarAdapter: CalendarAdapter? = null

    private var selectedYear = 1404
    private var selectedMonth = 6

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        listener()
        observe()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun init() {
        setButtons()
        setBottomSheet() // فقط اینجا فراخوانی کن
        setRecyclerView()
        setSpinner()

        lifecycleScope.launch {
            viewModel.getTasksInMonth(selectedYear, selectedMonth).collect { tasks ->
                withContext(Dispatchers.Main) {
                    items = tasks
                    updateCalendar()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateCalendar() {
        if (selectedCalendarType == "هفتگی") {
            binding.weeklyCalendar.visibility = View.VISIBLE
            binding.monthlyCalendar.visibility = View.GONE
            setWeeklyCalendar()
        } else {
            binding.weeklyCalendar.visibility = View.GONE
            binding.monthlyCalendar.visibility = View.VISIBLE
            setMonthlyCalendar()
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun setMonthlyCalendar(){
        binding.apply {
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
                    setBottomSheet()
                }
            }

            binding.tvMonth.text = "${persionMonth[selectedMonth - 1]} $selectedYear"
        }
    }

    private fun setWeeklyCalendar() {
        binding.apply {
            items?.let { tasks ->

                    val weeks = getWeeksRange(tasks = tasks)
                    val weeklyAdapter = WeeklyAdapter(
                        weeks = weeks,
                        context = requireContext()
                    )
                    rcWeeklyDays.adapter = weeklyAdapter

                    weeklyAdapter.setOnMonthYearChangedListener { month, year ->
                        tvSelectedMonth.text = month
                        tvSelectedYear.text = year
                    }
                    rcWeeklyDays.offscreenPageLimit = 1
                    rcWeeklyDays.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                        override fun onPageSelected(position: Int) {
                            super.onPageSelected(position)
                            weeklyAdapter.setCurrentWeek(position) // تنظیم هفته فعلی
                        }
                    })

                    if (rcWeeklyDays.currentItem >= weeks.size - 2) {
                        val newWeeks = getWeeksRange(tasks, weeksBefore = 0, weeksAfter = 4)
                        (rcWeeklyDays.adapter as WeeklyAdapter).addWeeks(newWeeks)
                    } else if (rcWeeklyDays.currentItem <= 2) {
                        val newWeeks = getWeeksRange(tasks, weeksBefore = 4, weeksAfter = 0)
                        (rcWeeklyDays.adapter as WeeklyAdapter).addWeeksBefore(newWeeks)
                    }
                    val currentWeekIndex = weeks.size / 2
                    rcWeeklyDays.currentItem = currentWeekIndex
                    weeklyAdapter.setCurrentWeek(currentWeekIndex) // تنظیم هفته اولیه
                    setBottomSheet()
            }
        }
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
        val habitsOfDay = habits.filter { it.shouldShowOn(CalendarDay(
            day, selectedMonth, selectedYear, true,
            isToday = false
        )) }
        showTasks(tasksOfDays)
        showHabits(habitsOfDay)
    }

    private fun showTasks(tasks: List<TaskModel>) {
        if (tasks.count() == 0){
            binding.rcTasks.visibility = View.GONE
            //binding.tvNoTasks.visibility = View.VISIBLE
        }else {
            binding.rcTasks.visibility = View.VISIBLE
            //binding.tvNoTasks.visibility = View.GONE
            val adapter = TaskListAdapter(tasks, requireActivity(), viewModel)
            binding.rcTasks.layoutManager = LinearLayoutManager(requireContext())
            binding.rcTasks.adapter = adapter
        }
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

    private fun showTasks() {
        lifecycleScope.launch {
            viewModel.tasks.collect { tasks ->
                val adapter = TaskListAdapter(tasks, requireActivity(), viewModel)
                binding.rcTasks.layoutManager = LinearLayoutManager(requireContext())
                binding.rcTasks.adapter = adapter
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showHabits() {
        lifecycleScope.launch {
            hobitViewModel.habits.collect { habits ->
                val today = CalendarDay(
                    15, 6, 1404, true,
                    tasks = items,
                    habits = habits,
                    percentageTaskHasBeenDone = 0,
                    isToday = false
                )
                val todayHabits = habits.filter { it.shouldShowOn(today) }

                val adapter = HabitListAdapter(todayHabits, requireActivity(), hobitViewModel)
                binding.rcTasks.layoutManager = LinearLayoutManager(requireContext())
                binding.rcTasks.adapter = adapter
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun listener() {
        binding.apply {
            imgAddNew.setOnClickListener {
                newTask()
            }
            binding.btnPrev.setOnClickListener { moveToPrevMonth() }
            binding.btnNext.setOnClickListener { moveToNextMonth() }

        }
    }

    private fun observe() {

    }

    private fun setRecyclerView() {
        items = viewModel.tasks.value
        binding.apply {
            items?.let { tasks ->
                viewModel.viewModelScope.launch {
                    viewModel.tasks.collect { it ->
                        val adapter = TaskListAdapter(it, requireActivity(), viewModel)
                        rcTasks.layoutManager = LinearLayoutManager(requireContext())

                        rcTasks.adapter = adapter
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun moveToPrevMonth() {
        if (selectedMonth == 1) {
            selectedMonth = 12
            selectedYear--
        } else {
            selectedMonth--
        }
        setMonthlyCalendar()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun moveToNextMonth() {
        if (selectedMonth == 12) {
            selectedMonth = 1
            selectedYear++
        } else {
            selectedMonth++
        }
        setMonthlyCalendar()
    }

    private fun setBottomSheet() {
        binding.apply {

            val behavior = BottomSheetBehavior.from(bottomSheet)
            topContent.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    topContent.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    val topHeight = topContent.height
                    val paddingBottomPx = topContent.paddingBottom
                    val displayMetrics = resources.displayMetrics
                    val screenHeight = displayMetrics.heightPixels
                    val contentHeight = topHeight - paddingBottomPx
                    val peekHeight = screenHeight - contentHeight
                    behavior.peekHeight = peekHeight
                    behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            })
        }
    }

    private fun newTask(){
        val dialog = HabitBottomSheetDialog { habitData,taskData ->

            habitData?.let {
                val habit = HabitModel(
                    title = habitData.title,
                    desc = habitData.desc,
                    repeatType = habitData.repeatType,
                    daysOfWeek = habitData.daysOfWeek,
                    dayOfMonth = habitData.dayOfMonth,
                    monthOfYear = habitData.monthOfYear,
                    dayOfYear = habitData.dayOfYear
                )
                habitViewModel.addHabit(habit)
            }
            taskData?.let {
                var newId = taskViewModel.addTask(taskData)
                taskData.id = newId.toInt()
                TaskScheduler.scheduleTaskNotification(context = requireContext(), taskData)
            }

        }
        dialog.show(parentFragmentManager, "HabitBottomSheet")
    }

    private fun setSpinner() {
        binding.apply {
            val menuItems = listOf("هفتگی", "ماهانه")
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                menuItems
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            drpCalendarType.adapter = adapter

            drpCalendarType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    selectedCalendarType = menuItems[position]
                    updateCalendar() // به‌روزرسانی تقویم
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun setButtons() {
        binding.apply {
            btnTasks.setOnClickListener {
                showTasks()
                btnHabits.setBackgroundColor(requireContext().resources.getColor(R.color.light_primary_variant))
                btnHabits.setTextColor(requireContext().resources.getColor(R.color.light_text_primary))
                btnTasks.setBackgroundColor(requireContext().resources.getColor(R.color.light_primary))
                btnTasks.setTextColor(requireContext().resources.getColor(R.color.dark_text_primary))
            }

            btnHabits.setOnClickListener {
                showHabits()
                btnTasks.setBackgroundColor(requireContext().resources.getColor(R.color.light_primary_variant))
                btnTasks.setTextColor(requireContext().resources.getColor(R.color.light_text_primary))
                btnHabits.setBackgroundColor(requireContext().resources.getColor(R.color.light_primary))
                btnHabits.setTextColor(requireContext().resources.getColor(R.color.dark_text_primary))
            }
        }
    }

}