package com.care.locationpoc

import android.app.Application
import androidx.annotation.NonNull
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.care.locationpoc.data.LogRepository
import com.care.locationpoc.geofence.GeofenceClient

class ViewModelFactory(
    private val repository: LogRepository,
    private val geofenceClient: GeofenceClient,
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return  MainViewModel(repository, geofenceClient) as T
    }
}