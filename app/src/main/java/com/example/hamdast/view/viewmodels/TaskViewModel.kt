package com.example.hamdast.view.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hamdast.data.models.TaskModel
import com.example.hamdast.data.repos.TasksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor( private val userRepository: TasksRepository): ViewModel() {

    private val _tasks = MutableStateFlow<List<TaskModel>>(emptyList())
    val tasks: StateFlow<List<TaskModel>> = _tasks
    init {
        viewModelScope.launch {
            userRepository.tasks.collect {
                _tasks.value = it
            }
        }
    }

    fun addTask(taskModel: TaskModel) {
        viewModelScope.launch {
            userRepository.addNewTask(taskModel)
        }
    }

    fun updateTaskDone(id: Int,isDone: Boolean)
    {
        viewModelScope.launch {
            userRepository.updateTask(id,isDone)
        }
    }

    fun deleteTask(id:Int){
        viewModelScope.launch {
            userRepository.deleteTask(id = id)
        }
    }

}