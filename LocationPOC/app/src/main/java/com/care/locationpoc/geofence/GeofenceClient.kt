package com.care.locationpoc.geofence

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices

class GeofenceClient(private val context: Context) {

    private var geofencingClient = LocationServices.getGeofencingClient(context)

    @SuppressLint("MissingPermission")
    fun addGeofence(geofences: List<GeofenceData>) {
        geofencingClient.addGeofences(getGeofencingRequest(geofences), createGeofencePendingIntent())
            .run {
            addOnSuccessListener {
                Toast.makeText(context, "Geofence created successfully!", Toast.LENGTH_SHORT).show()
            }
            addOnFailureListener {
                Toast.makeText(context, "Geofence failed!", Toast.LENGTH_SHORT).show()
                 it.printStackTrace()
            }
        }
    }

    fun removeGeofence() {
        geofencingClient.removeGeofences(createGeofencePendingIntent()).run {
            addOnSuccessListener {
                Toast.makeText(context, "Geofence created successfully!", Toast.LENGTH_SHORT).show()
            }
            addOnFailureListener {
                Toast.makeText(context, "Geofence failed!", Toast.LENGTH_SHORT).show()
                it.printStackTrace()
            }
        }
    }

    private fun getGeofencingRequest(geofences: List<GeofenceData>): GeofencingRequest {
        if (geofences.size > 100) {
            throw Exception("Geofences active limited archived.")
        }
        return GeofencingRequest.Builder().apply {
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            addGeofences(createGeofenceList(geofences))
        }.build()
    }

    private fun createGeofenceList(geofences: List<GeofenceData>): List<Geofence> {
        val geofenceList = arrayListOf<Geofence>()
        geofences.forEach { geofence ->
            geofenceList.add(
                Geofence.Builder()
                    .setRequestId(geofence.key)
                    .setCircularRegion(
                        geofence.latitude,
                        geofence.longitude,
                        geofence.radiusInMeters
                    )
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build()
            )
        }

        return geofenceList
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun createGeofencePendingIntent(): PendingIntent {
        val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE)
    }

}