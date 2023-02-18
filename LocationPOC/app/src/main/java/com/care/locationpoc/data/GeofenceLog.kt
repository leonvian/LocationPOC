package com.care.locationpoc.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng

@Entity
data class GeofenceLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val geofenceKey: String,
    val event: String,
    val timeStamp: String,
    val latitude: Double,
    val longitude: Double
) {

    fun toLatLng(): LatLng = LatLng(latitude, longitude)

}