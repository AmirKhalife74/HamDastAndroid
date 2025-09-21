package com.example.hamdast.app

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.hamdast.utils.notifications.DailySchedulerWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class HamdastApp:Application() {
    override fun onCreate() {
        super.onCreate()

        val workRequest = PeriodicWorkRequestBuilder<DailySchedulerWorker>(
            1, TimeUnit.DAYS
        )
            .setInitialDelay(2, TimeUnit.HOURS) // اجرای اولیه بعد از ۲ ساعت (مثلا ۲ شب)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "daily_scheduler",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}