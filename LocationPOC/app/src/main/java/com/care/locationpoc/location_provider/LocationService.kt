package com.care.locationpoc.location_provider

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.care.locationpoc.R
import com.care.locationpoc.data.LogRepository
import com.care.locationpoc.data.toLog
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class LocationService : Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: LocationClient
    private lateinit var repository: LogRepository

    override fun onCreate() {
        super.onCreate()
        locationClient = LocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )

        repository = LogRepository(LocationApplication.database!!)

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)

    }

    private fun start() {
        Log.i("[LOCATION]", "Service Started!!")
        val notification =
            NotificationCompat.Builder(this, NOTIFICATION_LOCATION_CHANNEL)
                .setContentTitle("Tracking Location")
                .setContentText("Location: ()")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setOngoing(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        locationClient.getLocationUpdates(10000L)
            .catch { e -> e.printStackTrace() }
            .onEach { location ->

                serviceScope.launch {
                    repository.saveLocation(location.toLog())
                }

                val lat = location.latitude.toString()
                val lng = location.longitude.toString()
                val updatedNotification = notification.setContentText(
                    "Location: ($lat, $lng)"
                )
                notificationManager.notify(NOTIFICATION_ID, updatedNotification.build())
            }
            .launchIn(serviceScope)


        startForeground(NOTIFICATION_ID, notification.build())
    }

    private fun stop() {
        stopForeground(true)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    override fun onBind(p0: Intent?): IBinder? = null


    companion object {
        const val NOTIFICATION_LOCATION_CHANNEL = "locationId"
        const val NOTIFICATION_ID = 1
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }

}