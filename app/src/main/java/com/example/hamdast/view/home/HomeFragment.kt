package com.example.hamdast.view.home

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources.getColorStateList
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hamdast.R
import com.example.hamdast.data.models.TaskModel
import com.example.hamdast.databinding.FragmentHomeBinding
import com.example.hamdast.view.home.adapter.TaskListAdapter
import com.example.hamdast.view.viewmodels.TaskViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: TaskViewModel by viewModels()
    private lateinit var binding:FragmentHomeBinding
    private var items:List<TaskModel>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        listener()
        observe()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater,container,false)
        return binding.root
    }


    private fun init(){
        setProgress()
        setButtons()
        setBottomSheet()
        setRecyclerView()


    }

    private fun listener(){

    }

    private fun observe(){

    }

    private fun setRecyclerView()
    {
        items = viewModel.tasks.value
        binding.apply {
            items?.let { tasks ->
                viewModel.viewModelScope.launch {
                    viewModel.tasks.collect { it ->
                        val adapter = TaskListAdapter(it,requireActivity(),viewModel)
                        rcTasks.layoutManager = LinearLayoutManager(requireContext())
                        rcTasks.adapter = adapter
                    }
                }


            }

        }
    }

    @SuppressLint("SetTextI18n")
    private fun setProgress()
    {
        binding.apply {
            val progress = 68
            circularProgressBar.setProgress(progress, true)
            progressText.text = "$progress%"
        }
    }

    private fun setBottomSheet()
    {
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


    private fun setButtons()
    {
        binding.apply {
            btnTasks.setOnClickListener {
                btnHabits.setBackgroundColor(requireContext().resources.getColor(R.color.light_primary_variant))
                btnHabits.setTextColor(requireContext().resources.getColor( R.color.light_text_primary))
                btnTasks.setBackgroundColor(requireContext().resources.getColor(R.color.light_primary))
                btnTasks.setTextColor(requireContext().resources.getColor(R.color.dark_text_primary))
            }

            btnHabits.setOnClickListener {
                btnTasks.setBackgroundColor(requireContext().resources.getColor(R.color.light_primary_variant))
                btnTasks.setTextColor(requireContext().resources.getColor( R.color.light_text_primary))
                btnHabits.setBackgroundColor(requireContext().resources.getColor(R.color.light_primary))
                btnHabits.setTextColor(requireContext().resources.getColor(R.color.dark_text_primary))
            }
        }
    }

}