package com.care.locationpoc

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.care.locationpoc.data.LocationLog
import com.care.locationpoc.data.LogRepository
import com.care.locationpoc.geofence.GeofenceBroadcastReceiver
import com.care.locationpoc.geofence.GeofenceClient
import com.care.locationpoc.location_provider.LocationApplication
import com.care.locationpoc.location_provider.LocationService
import com.care.locationpoc.ui.theme.LocationPOCTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by lazy {
        ViewModelFactory(
            LogRepository(LocationApplication.database!!), GeofenceClient(this)
        ).create(MainViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            LocationPOCTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column {
                        RowOptions(
                            onListLocationClicked = {
                                viewModel.loadAllLocations()
                            },
                            onGeofenceClicked = {
                                viewModel.createGeofenceForHome()
                            },
                            onGeofenceLogs = {
                                viewModel.loadGeofenceLogs()
                            }
                        )

                        CareMap(viewModel.locations.value)
                    }
                }
            }
        }
    }

    @Composable
    private fun CareMap(locations: List<LocationLog>) {
        val home = LatLng(-19.764973, -43.8435774)

        val cameraPositionState = rememberCameraPositionState {
            position =
                CameraPosition.fromLatLngZoom(locations.lastOrNull()?.toLatLng() ?: home, 15f)
        }

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            Circle(center = LatLng(-19.764973, -43.8435774), radius = 100.0)

            locations.forEach {
                Marker(
                    state = MarkerState(position = it.toLatLng()),
                    title = "Test",
                    snippet = "${it.provider} ${it.accuracy}"
                )
            }
        }
    }

    @Composable
    private fun RowOptions(
        onListLocationClicked: () -> Unit,
        onGeofenceClicked: () -> Unit,
        onGeofenceLogs: () -> Unit
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .horizontalScroll(state = rememberScrollState(), enabled = true)
        ) {
            ButtonSpaced(onClick = {
                requestPermission()
            }, text = "Permission")

            ButtonSpaced(onClick = {
                Intent(applicationContext, LocationService::class.java).apply {
                    action = LocationService.ACTION_START
                    startService(this)
                }
            }, text = "Start")

            ButtonSpaced(onClick = {
                Intent(applicationContext, LocationService::class.java).apply {
                    action = LocationService.ACTION_STOP
                    startService(this)
                }
            }, text = "Stop")

            Spacer(modifier = Modifier.size(8.dp))

            ButtonSpaced(onClick = onListLocationClicked, text = "List Locations")
            ButtonSpaced(onClick = onGeofenceClicked, text = "Create Geofence")
            ButtonSpaced(onClick = onGeofenceLogs, text = "List Geofence Logs")
        }
    }

    @Composable
    private fun ButtonSpaced(onClick: () -> Unit, text: String) {
        Spacer(modifier = Modifier.size(8.dp))

        Button(onClick = onClick) {
            Text(text = text)
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION//,
                //Manifest.permission.ACCESS_BACKGROUND_LOCATION,

            ),
            0
        )
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LocationPOCTheme {
    }
}