package com.example.hamdast.utils.notifications

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.hamdast.data.models.task.TaskModel
import com.example.hamdast.worker.NotificationWorker
import java.util.concurrent.TimeUnit

object TaskScheduler {

    fun scheduleTaskNotification(context: Context, task: TaskModel) {
        val now = System.currentTimeMillis()

        // نوتیفیکیشن سر زمان اصلی
        if (task.timeInTimeStamp > now) {
            val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInitialDelay(task.timeInTimeStamp - now, TimeUnit.MILLISECONDS)
                .setInputData(
                    workDataOf(
                        "title" to task.title,
                        "message" to "زمان انجام ${task.title} رسیده ⏰",
                        "id" to task.id
                    )
                )
                .build()

            WorkManager.getInstance(context).enqueue(workRequest)
        }

        // نوتیفیکیشن ۲۴ ساعت قبل
        val before = task.timeInTimeStamp - 24*60*60*1000
        if (before > now) {
            val workRequestBefore = OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInitialDelay(before - now, TimeUnit.MILLISECONDS)
                .setInputData(
                    workDataOf(
                        "title" to "${task.title} (یادآوری)",
                        "message" to "یادت نره فردا ${task.title} داری",
                        "id" to task.id + 1000
                    )
                )
                .build()

            WorkManager.getInstance(context).enqueue(workRequestBefore)
        }
    }
}
