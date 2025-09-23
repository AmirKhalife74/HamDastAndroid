package com.example.hamdast.view.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hamdast.data.models.task.TaskModel
import com.example.hamdast.data.repos.TasksRepository
import com.example.hamdast.utils.daysInMonth
import com.example.hamdast.utils.persianToGregorian
import com.example.hamdast.utils.twoDigitConvertor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
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

    fun addTask(taskModel: TaskModel):Long {
        var newId:Long = 0
        viewModelScope.launch {
            newId = userRepository.addNewTask(taskModel)

        }
        return newId
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

    fun getTasksByDate(date: String): Flow<List<TaskModel>> {
        return userRepository.getTasksByDate(date)
    }

    fun getTasksInMonth(year: Int, month: Int): Flow<List<TaskModel>> = flow {
        val (gyStart, gmStart, gdStart) = persianToGregorian(year, month, 1)
        val (gyEnd, gmEnd, gdEnd) = persianToGregorian(year, month, daysInMonth[month - 1])

        val startDate = "$gyStart-${twoDigitConvertor(gmStart)}-${twoDigitConvertor(gdStart)}"
        val endDate = "$gyEnd-${twoDigitConvertor(gmEnd)}-${twoDigitConvertor(gdEnd)}"

        userRepository.getTasksByMonth(year, month).collect { tasks ->
            emit(tasks)
        }
    }



}