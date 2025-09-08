package com.example.hamdast.data.repos

import HabitLog
import com.example.hamdast.data.database.HabitsLogDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HabitLogsRepository @Inject constructor(
    private val habitLogsDao: HabitsLogDao
) {
    fun getLogsForDay(year: Int, month: Int, day: Int): Flow<List<HabitLog>> {
        return habitLogsDao.getLogsForDay(year, month, day)
    }

    suspend fun addLog(log: HabitLog) {
        habitLogsDao.addLog(log)
    }

    suspend fun updateLog(log: HabitLog) {
        habitLogsDao.updateLog(log)
    }

    suspend fun deleteLog(log: HabitLog) {
        habitLogsDao.deleteLog(log)
    }

    suspend fun deleteLogsByHabitId(habitId: Int) {
        habitLogsDao.deleteLogsByHabitId(habitId)
    }
}
