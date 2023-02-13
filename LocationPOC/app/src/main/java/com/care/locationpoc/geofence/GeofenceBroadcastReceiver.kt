package com.care.locationpoc.geofence

import android.app.usage.UsageEvents.Event
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.care.locationpoc.data.GeofenceLog
import com.care.locationpoc.data.LogRepository
import com.care.locationpoc.location_provider.LocationApplication
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class GeofenceBroadcastReceiver : BroadcastReceiver() {

    private val logRepository = LogRepository(LocationApplication.database!!)

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onReceive(context: Context?, intent: Intent) {
        Log.i(TAG, "Broad cast receiver activated!!")

        val geofencingEvent = GeofencingEvent.fromIntent(intent)

         if (geofencingEvent == null || geofencingEvent.hasError()) {
            val errorMessage =
                GeofenceStatusCodes.getStatusCodeString(geofencingEvent?.errorCode ?: 0)
            serviceScope.launch {
                val geofenceLog = GeofenceLog(
                    geofenceKey = "Error",
                    event = errorMessage,
                    timeStamp = nowInString()
                )
                logRepository.saveGeofence(geofenceLog)
            }
            return
        }
        Log.i(TAG, "Broad cast receiver activated!! Not Null")
        // Get the transition type.
        val geofenceTransition = geofencingEvent.geofenceTransition
        val triggeringGeofences = geofencingEvent.triggeringGeofences

        val eventName = when (geofenceTransition) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> "Entering"
            Geofence.GEOFENCE_TRANSITION_EXIT -> "Exiting"
            Geofence.GEOFENCE_TRANSITION_DWELL -> "Dwell"
            else -> "Unknown"
        }

        Toast.makeText(context, "Geofence event: ${eventName}", Toast.LENGTH_SHORT).show()

        serviceScope.launch {
            geofencingEvent.triggeringGeofences?.forEach {
                val geofenceLog = GeofenceLog(
                    geofenceKey = it.requestId,
                    event = eventName,
                    timeStamp = nowInString()
                )
                logRepository.saveGeofence(geofenceLog)
            }
        }

        Log.i(TAG, "Transition = $eventName Geofence Triggered = $triggeringGeofences")

    }


    private fun nowInString(): String {
        val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val now = Date()
        val dateString = parser.format(now)

        return dateString
    }

    companion object {
        const val TAG = "[GEOFENCE]"
    }

}