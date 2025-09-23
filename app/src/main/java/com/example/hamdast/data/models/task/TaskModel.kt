package com.example.hamdast.data.models.task

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.hamdast.data.models.task.TaskCategory

@Entity(tableName = "tasks")
class TaskModel (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title: String,
    var desc:String,
    var isDone: Boolean = false,
    var year: Int,
    var month: Int,
    var day: Int,
    var time: String,
    var timeInTimeStamp: Long,
    var date: String = "$year/$month/$day",
    var category: TaskCategory
)