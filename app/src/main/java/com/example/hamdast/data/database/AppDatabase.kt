package com.example.hamdast.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.hamdast.data.database.convertors.Converters
import com.example.hamdast.data.models.habit.HabitModel
import com.example.hamdast.data.models.task.TaskModel


@Database(entities = [TaskModel::class, HabitModel::class], version = 2)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun habitDao(): HabitsDao
}
