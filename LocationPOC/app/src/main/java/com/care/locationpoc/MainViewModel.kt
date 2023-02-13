package com.care.locationpoc

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.care.locationpoc.data.LocationLog
import com.care.locationpoc.data.LogRepository
import com.care.locationpoc.geofence.GeofenceClient
import com.care.locationpoc.geofence.GeofenceData
import com.google.android.gms.location.Geofence
import kotlinx.coroutines.launch
import kotlin.math.log

class MainViewModel(
    private val logRepository: LogRepository,
    private val geofenceClient: GeofenceClient
) : ViewModel() {

    val locations = mutableStateOf(listOf<LocationLog>())

    fun loadAllLocations() {
        viewModelScope.launch {
          locations.value = logRepository.getAllLocationLogs()
        }
    }

    //-19.764973 lng: -43.8435774
    fun createGeofenceForHome() {
        val hourInMilis: Long = 60000 * 60
        val geofenceExpiration: Long = hourInMilis * 24
        val geofences = listOf<GeofenceData>(
            GeofenceData("My home",-19.764973, -43.8435774, 100.0f, Geofence.NEVER_EXPIRE)
        )
        geofenceClient.addGeofence(geofences)
    }

    fun loadGeofenceLogs() {
        viewModelScope.launch {
            val logs = logRepository.getAllGeofenceLogs()
            logs.forEach {
                Log.i("[GEOFENCE]", "${it.geofenceKey} ${it.event} ${it.timeStamp}")
            }
            Log.i("[GEOFENCE]", "Logs size ${logs.size}")
        }
    }

}