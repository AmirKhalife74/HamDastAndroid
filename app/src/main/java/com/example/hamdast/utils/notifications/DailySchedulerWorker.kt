package com.example.hamdast.utils.notifications

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.hamdast.data.database.HabitsDao
import com.example.hamdast.data.database.TaskDao
import com.example.hamdast.data.models.habit.HabitModel
import com.example.hamdast.data.models.habit.RepeatType
import com.example.hamdast.data.models.task.TaskModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.*

class DailySchedulerWorker(
    private val context: Context,
    params: WorkerParameters,
    private val taskDao: TaskDao,
    private val habitDao: HabitsDao
) : CoroutineWorker(context, params) {

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val now = LocalDate.now()
        val tomorrow = now.plusDays(1)

        // --- تسک‌ها
        val tasks = taskDao.getAllTasks()
        tasks.collect { tasks ->
            tasks.forEach { task ->
                handleTask(task, now, tomorrow)
            }
        }

        // --- هابیت‌ها
        val habits = habitDao.getAllHabits()
        habits.collect { habits ->
            habits.forEach { habit ->
            handleHabit(habit, now, tomorrow) }
        }

        Result.success()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleTask(task: TaskModel, today: LocalDate, tomorrow: LocalDate) {
        val taskDate = Instant.ofEpochMilli(task.timeInTimeStamp)
            .atZone(ZoneId.systemDefault()).toLocalDate()

        if (taskDate == today) {
            NotificationHelper.showNotification(
                context,
                task.title,
                "زمان انجام ${task.title} رسیده ⏰",
                task.id
            )
            NotificationHelper.showNotification(
                context,
                "${task.title} (یادآوری)",
                "فردا ${task.title} داری",
                task.id + 1000
            )
        } else if (taskDate == tomorrow) {
            NotificationHelper.showNotification(
                context,
                "${task.title} (یادآوری)",
                "یادت باشه فردا ${task.title} داری",
                task.id + 2000
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleHabit(habit: HabitModel, today: LocalDate, tomorrow: LocalDate) {
        if (shouldTriggerHabit(habit, today)) {
            NotificationHelper.showNotification(
                context,
                habit.title,
                "یادت نره امروز ${habit.title} انجام بدی 💪",
                habit.id + 10000
            )
        }

        if (shouldTriggerHabit(habit, tomorrow)) {
            NotificationHelper.showNotification(
                context,
                "${habit.title} (یادآوری)",
                "فردا ${habit.title} داری",
                habit.id + 20000
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun shouldTriggerHabit(habit: HabitModel, date: LocalDate): Boolean {
        return when (habit.repeatType) {
            RepeatType.DAILY -> true
            RepeatType.WEEKLY -> habit.daysOfWeek?.contains(date.dayOfWeek.value % 7) == true
            RepeatType.MONTHLY -> habit.dayOfMonth == date.dayOfMonth
            RepeatType.YEARLY -> (habit.monthOfYear == date.monthValue && habit.dayOfYear == date.dayOfMonth)
            RepeatType.CUSTOM -> date.dayOfMonth % 2 == 0 // مثلا روزهای زوج
        }
    }
}
