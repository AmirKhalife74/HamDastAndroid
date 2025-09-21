package com.example.hamdast.view.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.hamdast.R
import com.example.hamdast.data.models.HabitModel
import com.example.hamdast.data.models.TaskModel
import com.example.hamdast.databinding.FragmentMainBinding
import com.example.hamdast.utils.HabitBottomSheetDialog
import com.example.hamdast.utils.addNewTask
import com.example.hamdast.utils.notifications.TaskScheduler
import com.example.hamdast.view.viewmodels.HabitViewModel
import com.example.hamdast.view.viewmodels.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class MainFragment : Fragment() {

    private lateinit var  binding: FragmentMainBinding
    private val taskViewModel: TaskViewModel by viewModels()
    private val habitViewModel: HabitViewModel by viewModels()
    private var navHost: NavHostFragment? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        listen()
        observe()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMainBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    private fun init(){
        binding.apply {
            navHost =
                childFragmentManager.findFragmentById(R.id.nav_host_main_fragment) as NavHostFragment
            val navController = navHost!!.navController
            setupWithNavController(binding.bottomNavigationView, navController)

        }
    }
    private fun listen(){
        binding.apply {
            imgAddTask.setOnClickListener {
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
        }

    }

    private fun HabitBottomSheetDialog(function: (TaskModel) -> Unit) {


    }

    private fun observe(){}


}