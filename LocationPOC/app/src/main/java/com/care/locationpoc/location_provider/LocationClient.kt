package com.care.locationpoc.location_provider

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class LocationClient(
    private val context: Context,
    private val client: FusedLocationProviderClient
) {

    @SuppressLint("MissingPermission")
    fun getLocationUpdates(interval: Long): Flow<Location> {
        return callbackFlow {
            Log.i("[LOCATION]", "Start callback flow")
            throwExceptionIfThereIsNoLocationPermission()
            throwExceptionIfLocationIsDisabled()

            val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, interval)
                .build()

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    super.onLocationResult(result)
                    Log.i("[LOCATION]", "Location collected!")
                    result.locations.lastOrNull()?.let { location ->
                        launch {
                            send(location)
                        }
                    }
                }
            }

            Log.i("[LOCATION]", "Request Location Updates!")
            client.requestLocationUpdates(request, locationCallback, Looper.getMainLooper())

            awaitClose {
                client.removeLocationUpdates(locationCallback)
            }
        }
    }

    private fun throwExceptionIfThereIsNoLocationPermission() {
        if (!context.hasLocationPermission()) {
            throw Exception("Missing location permission")
        }
    }

    private fun throwExceptionIfLocationIsDisabled() {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (!isGpsEnabled && !isNetworkEnabled) {
            Log.i("[LOCATION]", "Location Disabled")
            throw Exception("Location disabled")
        }
    }

    private fun Context.hasLocationPermission(): Boolean {
        return hasPermissionGranted(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    private fun Context.hasPermissionGranted(vararg permisions: String): Boolean {
        permisions.forEach {
            if (ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED) {
                Log.i("[LOCATION]", "Permission Failed :$it")
                return false
            }
        }

        return true
    }


}