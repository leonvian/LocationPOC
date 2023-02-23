package com.care.locationpoc.workmanager

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.care.locationpoc.R
import com.care.locationpoc.data.LogRepository
import com.care.locationpoc.data.toLog
import com.care.locationpoc.location_provider.LocationApplication
import com.care.locationpoc.location_provider.LocationClient
import com.care.locationpoc.location_provider.LocationService
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LocationWork(
    private val context: Context,
    private val workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        setForeground(createForegroundInfo())
        delay(10000 * 10)
      //  startLocationService()
        return Result.success()
    }

    private fun startLocationService() {
        Intent(applicationContext, LocationService::class.java).apply {
            action = LocationService.ACTION_START
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(this)
            } else {
                context.startService(this)
            }
        }
    }

    private fun createForegroundInfo(): ForegroundInfo {

        // This PendingIntent can be used to cancel the worker
      //  val intent = WorkManager.getInstance(applicationContext)
       //     .createCancelPendingIntent(getId())

        val notification = NotificationCompat.Builder(applicationContext, LocationService.NOTIFICATION_LOCATION_CHANNEL)
            .setContentTitle("Test Work")
            .setTicker("Test Work")
            .setContentText("Test")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setOngoing(true)
            // Add the cancel action to the notification which can
            // be used to cancel the worker
          // .addAction(android.R.drawable.ic_delete, "cancel", intent)
            .build()

        return ForegroundInfo(LocationService.NOTIFICATION_ID, notification)
    }



}