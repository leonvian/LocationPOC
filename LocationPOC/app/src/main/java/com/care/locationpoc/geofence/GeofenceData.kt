package com.care.locationpoc.geofence

data class GeofenceData(
    val key: String, // // Set the request ID of the geofence. This is a string to identify this
    val latitude: Double,
    val longitude: Double,
    val radiusInMeters: Float,
    val durationInMillis: Long
)