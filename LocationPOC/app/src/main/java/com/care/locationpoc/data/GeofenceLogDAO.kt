package com.care.locationpoc.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GeofenceLogDAO {

    @Insert
    suspend fun insert(geofenceLog: GeofenceLog)

    @Insert
    suspend fun insertAll(geofenceLogs: List<GeofenceLog>)

    @Query("DELETE FROM GeofenceLog")
    suspend fun deleteAll()

    @Query("SELECT * FROM GeofenceLog")
    fun getAll(): Flow<List<GeofenceLog>>

}