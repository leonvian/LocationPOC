package com.care.locationpoc.workmanager

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.time.Duration

object LocationScheduler {

    @RequiresApi(Build.VERSION_CODES.O)
    fun scheduleWork(context: Context) {
        val request = OneTimeWorkRequestBuilder<LocationWork>()
            .setInitialDelay(Duration.ofSeconds(30))
            .build()
        WorkManager.getInstance(context)
            .beginUniqueWork(
                "work_location",
                ExistingWorkPolicy.KEEP,
                request
            ).enqueue()
    }

}