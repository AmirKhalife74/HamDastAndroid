package com.example.hamdast.view.home

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hamdast.R
import com.example.hamdast.data.models.CalendarDay
import com.example.hamdast.data.models.TaskModel
import com.example.hamdast.databinding.FragmentHomeBinding
import com.example.hamdast.utils.getCurrentWeek
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
            val bottomSheet = binding.bottomSheet
            val behavior = BottomSheetBehavior.from(bottomSheet)


//            behavior.peekHeight = 1500
//
//
//            behavior.isFitToContents = true
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED // حالت اولیه


            behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_EXPANDED -> {
                            // فول‌اسکرین شد
                        }

                        BottomSheetBehavior.STATE_COLLAPSED -> {
                            // برگشت پایین
                        }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    // انیمیشن یا افکت همزمان با درگ
                }
            })
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