package com.example.hamdast.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.hamdast.utils.notifications.NotificationHelper

class NotificationWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {
        val title = inputData.getString("title") ?: "یادآوری"
        val message = inputData.getString("message") ?: ""
        val id = inputData.getInt("id", 0)

        NotificationHelper.showNotification(applicationContext, title, message, id)
        return Result.success()
    }
}
