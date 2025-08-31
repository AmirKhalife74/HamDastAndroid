package com.example.hamdast.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.hamdast.R
import com.example.hamdast.data.models.TaskModel
import com.example.hamdast.databinding.FragmentHomeBinding
import com.example.hamdast.view.home.adapter.TaskListAdapter
import com.example.hamdast.view.viewmodels.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

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

}