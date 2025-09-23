package com.example.hamdast.view.home

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hamdast.R
import com.example.hamdast.data.models.calendar.CalendarDay
import com.example.hamdast.data.models.habit.HabitModel
import com.example.hamdast.data.models.task.TaskModel
import com.example.hamdast.databinding.FragmentHomeBinding
import com.example.hamdast.utils.HabitBottomSheetDialog
import com.example.hamdast.utils.getCurrentWeek
import com.example.hamdast.utils.notifications.TaskScheduler
import com.example.hamdast.utils.shouldShowOn
import com.example.hamdast.view.home.adapter.HabitListAdapter
import com.example.hamdast.view.home.adapter.TaskListAdapter
import com.example.hamdast.view.home.adapter.WeeklyDaysAdapter
import com.example.hamdast.view.viewmodels.HabitViewModel
import com.example.hamdast.view.viewmodels.TaskViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: TaskViewModel by viewModels()
    private val hobitViewModel: HabitViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    private var items: List<TaskModel>? = null
    private var currentDay: Int = 0
    private var currentMonth: Int = 0
    private var currentYear: Int = 0
    private val taskViewModel: TaskViewModel by viewModels()
    private val habitViewModel: HabitViewModel by viewModels()

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
        setProgress()
        setButtons()
        setBottomSheet()
        setRecyclerView()


        lifecycleScope.launch {
            viewModel.getTasksInMonth(1404, 6).collect { tasks ->
                items = tasks
                setWeeklyCalendar()
            }
        }

    }
    private fun setWeeklyCalendar() {
        binding.apply {
            items?.let { it ->
                if (it.isNotEmpty()){
                    val currentWeek = getCurrentWeek(tasks = it)
                    val weeklyDaysAdapter = WeeklyDaysAdapter(
                        items = currentWeek,
                        context = requireContext()
                    )
                    var layOutMnger = GridLayoutManager(requireContext(),7)
                    rcWeeklyDays.adapter = weeklyDaysAdapter
                    rcWeeklyDays.layoutManager = layOutMnger
                }else{

                }
            }
        }
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
                    percentageTaskHasBeenDone = 0
                ) // روز فعلی رو حساب کن
                val todayHabits = habits.filter { it.shouldShowOn(today) }

                val adapter = HabitListAdapter(todayHabits, requireActivity(), hobitViewModel)
                binding.rcTasks.layoutManager = LinearLayoutManager(requireContext())
                binding.rcTasks.adapter = adapter
            }
        }
    }

    private fun listener() {
        binding.apply {
            imgAddNew.setOnClickListener {
                newTask()
            }
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

    @SuppressLint("SetTextI18n")
    private fun setProgress() {
        binding.apply {
            val progress = 68
            circularProgressBar.setProgress(progress, true)
            progressText.text = "$progress%"
        }
    }

    private fun setBottomSheet() {
        binding.apply {

            val behavior = BottomSheetBehavior.from(bottomSheet)
            // Listener برای محاسبه ارتفاع بعد از layout
            topContent.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    // Listener رو حذف کن تا فقط یک بار اجرا بشه
                    topContent.viewTreeObserver.removeOnGlobalLayoutListener(this)

                    // ارتفاع کل top_content (معمولاً برابر screen height چون match_parent)
                    val topHeight = topContent.height

                    // محاسبه paddingBottom (در px)
                    val paddingBottomPx = topContent.paddingBottom

                    // ارتفاع واقعی محتوای top_content بدون paddingBottom
                    // اگر محتوای داخلی wrap_content باشه، می‌تونی جمع ارتفاع کودکان رو محاسبه کنی، اما ساده‌تر:
                    val displayMetrics = resources.displayMetrics
                    val screenHeight = displayMetrics.heightPixels
                    val contentHeight = topHeight - paddingBottomPx  // معمولاً screenHeight - paddingBottom

                    // تنظیم peekHeight برای باتم‌شیت: برابر با فضای خالی پایین top_content
                    // این کار باعث می‌شه در حالت collapsed، باتم‌شیت دقیقاً تا پایین محتوای top_content برسه
                    val peekHeight = screenHeight - contentHeight  // یا مستقیم paddingBottomPx اگر ثابت باشه
                    behavior.peekHeight = peekHeight

                    // اختیاری: شروع باتم‌شیت در حالت collapsed
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