package com.care.locationpoc

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.care.locationpoc.data.GeofenceLog
import com.care.locationpoc.data.LocationLog
import com.care.locationpoc.data.LogRepository
import com.care.locationpoc.geofence.GeofenceClient
import com.care.locationpoc.geofence.GeofenceData
import com.google.android.gms.location.Geofence
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.math.log

class MainViewModel(
    private val logRepository: LogRepository,
    private val geofenceClient: GeofenceClient
) : ViewModel() {

    val locations = mutableStateOf(listOf<LocationLog>())
    val geofenceLogs = mutableStateOf(listOf<GeofenceLog>())

    fun loadAllLocations() {
        viewModelScope.launch {
             logRepository.getAllLocationLogs().collect {
                 locations.value = it
             }
        }
    }

     fun deleteLocations() {
        viewModelScope.launch {
            logRepository.deleteAllLogs()
        }
    }


    @RequiresApi(Build.VERSION_CODES.M)
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
            logRepository.getAllGeofenceLogs().collect {
                geofenceLogs.value = it
                it.forEach {
                    Log.i("[GEOFENCE]", "${it.geofenceKey} ${it.event} ${it.timeStamp}")
                }
                Log.i("[GEOFENCE]", "Logs size ${it.size}")
            }

        }
    }

}