package com.example.hamdast.data.repos

import com.example.hamdast.data.database.TaskDao
import com.example.hamdast.data.models.TaskModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TasksRepository @Inject constructor(private val dao: TaskDao) {

    val tasks: Flow<List<TaskModel>> = dao.getAllTasks()

    suspend fun addNewTask(task:TaskModel){
        dao.insert(task = task)
    }

    suspend fun deleteTask(id: Int)
    {
        dao.delete(id = id)
    }

    suspend fun updateTask(id:Int,done:Boolean){

        dao.updateTaskStatus(id = id,done = done)
    }

    fun getTasksByDate(date: String): Flow<List<TaskModel>> {
        return dao.getTasksByDate(date)
    }

    fun getTasksByMonth(year: Int,month: Int): Flow<List<TaskModel>> {
        return dao.getTasksByMonth(year = year.toString(), month = month.toString())
    }

    fun getTasksUnMonth(startDate: String,endDate : String):Flow<List<TaskModel>> = flow{
        dao.getTasksBetween(startDate, endDate).collect { tasks ->
            emit(tasks)
        }
    }
}