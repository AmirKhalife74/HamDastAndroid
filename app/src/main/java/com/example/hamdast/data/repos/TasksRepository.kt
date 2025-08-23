package com.example.hamdast.data.repos

import com.example.hamdast.data.database.TaskDao
import com.example.hamdast.data.models.TaskModel
import kotlinx.coroutines.flow.Flow
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
}