package com.kristinaefros.challenge.presentation.location_service

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class PhotoLocationClient(
    private val client: FusedLocationProviderClient
) {
    @SuppressLint("MissingPermission")
    fun getLocationUpdates(interval: Long): Flow<Location> {
        return callbackFlow {

            val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, interval).apply {
                setMinUpdateDistanceMeters(0.1f)
                setWaitForAccurateLocation(true)
            }.build()

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    super.onLocationResult(result)
                    result.locations.firstOrNull()?.let { location ->
                        launch { send(location) }
                    }
                }
            }

            client.requestLocationUpdates(
                request,
                locationCallback,
                Looper.getMainLooper(),
            )

            awaitClose { client.removeLocationUpdates(locationCallback) }
        }
    }
}