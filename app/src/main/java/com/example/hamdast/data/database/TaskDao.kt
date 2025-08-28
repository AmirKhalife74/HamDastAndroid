package com.example.hamdast.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.hamdast.data.models.TaskModel
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: TaskModel)

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("SELECT * FROM tasks")
    fun getAllTasks(): Flow<List<TaskModel>>

    @Query("UPDATE tasks SET isDone = :done WHERE id = :id")
    suspend fun updateTaskStatus(id: Int, done: Boolean)

}