package com.care.locationpoc.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LocationLogDAO {

    @Insert
    suspend fun insert(locationLog: LocationLog)

    @Insert
    suspend fun insertAll(logs: List<LocationLog>)

    @Query("SELECT * FROM LocationLog")
    suspend fun getAll(): List<LocationLog>
}