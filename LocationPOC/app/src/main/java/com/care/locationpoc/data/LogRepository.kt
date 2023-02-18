package com.care.locationpoc.data

import kotlinx.coroutines.flow.Flow

class LogRepository(db: AppDatabase) {

    private val locationLogDAO = db.locationLogDao()

    private val geofenceLogDao = db.geofenceLogDao()

    suspend fun saveLocation(locationLog: LocationLog) {
        locationLogDAO.insert(locationLog)
    }

    suspend fun saveGeofence(geofenceLog: GeofenceLog) {
        geofenceLogDao.insert(geofenceLog)
    }

     fun getAllGeofenceLogs(): Flow<List<GeofenceLog>> {
        return geofenceLogDao.getAll()
    }

    fun getAllLocationLogs(): Flow<List<LocationLog>> {
        return locationLogDAO.getAll()
    }

    suspend fun deleteAllLogs() {
        geofenceLogDao.deleteAll()
        locationLogDAO.deleteAll()
    }

}