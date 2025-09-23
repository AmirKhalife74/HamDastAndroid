package com.example.hamdast.data.repos

import com.example.hamdast.data.database.HabitsDao
import com.example.hamdast.data.models.habit.HabitModel
import com.example.hamdast.data.models.habit.RepeatType
import com.example.hamdast.utils.daysInMonth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HabitsRepository @Inject constructor(
    private val habitsDao: HabitsDao
) {
    val habits: Flow<List<HabitModel>> = habitsDao.getAllHabits()

    suspend fun addHabit(habit: HabitModel) {
        habitsDao.addHabit(habit)
    }

    suspend fun deleteHabit(id: Int) {
        habitsDao.deleteHabitById(id)
    }

    suspend fun updateHabit(habit: HabitModel) {
        habitsDao.updateHabit(habit)
    }

    suspend fun getHabitById(id: Int): HabitModel? {
        return habitsDao.getHabitById(id)
    }

    fun getHabitsInMonth(year: Int, month: Int): Flow<List<HabitModel>> = flow {
        habitsDao.getAllHabits().collect { habits ->
            val filtered = habits.filter { habit ->
                when (habit.repeatType) {
                    RepeatType.DAILY -> true
                    RepeatType.WEEKLY -> {
                        // چک روزهای هفته
                        true // بررسی در زمان نمایش هر روز
                    }
                    RepeatType.MONTHLY -> habit.dayOfMonth != null && habit.dayOfMonth in 1..daysInMonth[month-1]
                    RepeatType.YEARLY -> habit.monthOfYear == month
                    RepeatType.CUSTOM -> true // بسته به الگویی که داری
                }
            }
            emit(filtered)
        }
    }
}