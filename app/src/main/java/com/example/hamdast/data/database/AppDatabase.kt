package com.example.hamdast.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.hamdast.data.models.TaskModel


@Database(entities = [TaskModel::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun taskDao(): TaskDao
}