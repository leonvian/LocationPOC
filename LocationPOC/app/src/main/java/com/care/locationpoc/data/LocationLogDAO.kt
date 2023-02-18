package com.care.locationpoc.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationLogDAO {

    @Insert
    suspend fun insert(locationLog: LocationLog)

    @Insert
    suspend fun insertAll(logs: List<LocationLog>)

    @Query("SELECT * FROM LocationLog")
    fun getAll(): Flow<List<LocationLog>>

    @Query("DELETE FROM LocationLog")
    suspend fun deleteAll()
}