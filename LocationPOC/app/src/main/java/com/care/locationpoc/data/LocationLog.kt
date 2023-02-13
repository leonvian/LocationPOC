package com.care.locationpoc.data

import android.location.Location
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import java.text.SimpleDateFormat
import java.util.*

@Entity
class LocationLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val latitude: String,
    val longitude: String,
    val accuracy: String,
    val provider: String,
    val timeStamp: String
) {
    fun toLatLng(): LatLng = LatLng(latitude.toDouble(), longitude.toDouble())
}

fun Location.toLog(): LocationLog {
    val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val now = Date()
    val dateString = parser.format(now)
    return LocationLog(
        latitude = this.latitude.toString(),
        longitude = this.longitude.toString(),
        accuracy = this.accuracy.toString(),
        provider = this.provider.toString(),
        timeStamp = dateString
    )
}

