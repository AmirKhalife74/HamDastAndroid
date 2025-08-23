package com.example.hamdast.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
class TaskModel (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val isDone: Boolean = false
)