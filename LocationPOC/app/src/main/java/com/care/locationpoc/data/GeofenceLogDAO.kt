package com.care.locationpoc.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface GeofenceLogDAO {

    @Insert
    suspend fun insert(geofenceLog: GeofenceLog)

    @Insert
    suspend fun insertAll(geofenceLogs: List<GeofenceLog>)

    @Query("SELECT * FROM GeofenceLog")
    suspend fun getAll(): List<GeofenceLog>

}