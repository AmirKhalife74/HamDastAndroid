package com.example.hamdast.data.models.habit

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habit_logs")
data class HabitLog(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val habitId: Int,
    val year: Int,
    val month: Int,
    val day: Int,
    val timesDone: Int = 0 )