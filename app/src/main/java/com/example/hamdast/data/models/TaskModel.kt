package com.example.hamdast.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

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
    var time:Double,
    var date: String = "$year/$month/$day"
)