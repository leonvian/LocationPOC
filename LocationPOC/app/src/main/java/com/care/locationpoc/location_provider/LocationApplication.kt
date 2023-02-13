package com.care.locationpoc.location_provider

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.care.locationpoc.data.AppDatabase

class LocationApplication : Application() {

    companion object {
      var database: AppDatabase? = null
    }

    override fun onCreate() {
        super.onCreate()
        database = AppDatabase.createDatabase(applicationContext)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                LocationService.NOTIFICATION_LOCATION_CHANNEL,
                "Location",
                NotificationManager.IMPORTANCE_LOW
            )

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}