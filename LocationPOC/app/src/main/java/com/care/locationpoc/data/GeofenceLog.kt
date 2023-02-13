package com.care.locationpoc.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GeofenceLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val geofenceKey: String,
    val event: String,
    val timeStamp: String
)