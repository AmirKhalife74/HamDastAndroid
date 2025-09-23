package com.example.hamdast.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.hamdast.data.models.habit.HabitModel
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitsDao {
    @Query("SELECT * FROM habits")
    fun getAllHabits(): Flow<List<HabitModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addHabit(habit: HabitModel)

    @Delete
    suspend fun deleteHabit(habit: HabitModel)

    @Query("DELETE FROM habits WHERE id = :id")
    suspend fun deleteHabitById(id: Int)

    @Update
    suspend fun updateHabit(habit: HabitModel)

    @Query("SELECT * FROM habits WHERE id = :id LIMIT 1")
    suspend fun getHabitById(id: Int): HabitModel?

}