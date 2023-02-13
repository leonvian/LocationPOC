package com.care.locationpoc.data

import android.content.Context

class LogRepository(db: AppDatabase) {

    private val locationLogDAO = db.locationLogDao()

    private val geofenceLogDao = db.geofenceLogDao()

    suspend fun saveLocation(locationLog: LocationLog) {
        locationLogDAO.insert(locationLog)
    }

    suspend fun saveGeofence(geofenceLog: GeofenceLog) {
        geofenceLogDao.insert(geofenceLog)
    }

    suspend fun getAllGeofenceLogs(): List<GeofenceLog> {
        return geofenceLogDao.getAll()
    }

    suspend fun getAllLocationLogs(): List<LocationLog> {
        return locationLogDAO.getAll()
    }

}