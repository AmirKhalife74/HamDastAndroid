package com.example.hamdast.data.database

import com.example.hamdast.data.models.habit.HabitLog
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

interface HabitsLogDao {
    @Query("SELECT * FROM habit_logs WHERE year = :year AND month = :month AND day = :day")
    fun getLogsForDay(year: Int, month: Int, day: Int): Flow<List<HabitLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addLog(log: HabitLog)

    @Update
    suspend fun updateLog(log: HabitLog)

    @Delete
    suspend fun deleteLog(log: HabitLog)

    @Query("DELETE FROM habit_logs WHERE habitId = :habitId")
    suspend fun deleteLogsByHabitId(habitId: Int)
}