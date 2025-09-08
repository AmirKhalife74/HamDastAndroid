package com.example.hamdast.data.di

import com.example.hamdast.data.database.HabitsDao
import com.example.hamdast.data.database.TaskDao
import com.example.hamdast.data.repos.HabitsRepository
import com.example.hamdast.data.repos.TasksRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideTasksRepository(taskDao: TaskDao): TasksRepository {
        return TasksRepository(taskDao)
    }

    @Provides
    @Singleton
    fun provideHabitRepository(habitsDao: HabitsDao): HabitsRepository {
        return HabitsRepository(habitsDao)
    }
}