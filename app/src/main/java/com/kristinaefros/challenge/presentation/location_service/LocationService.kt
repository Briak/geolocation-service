package com.kristinaefros.challenge.presentation.location_service

import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.LocationServices
import com.kristinaefros.challenge.R
import com.kristinaefros.challenge.domain.places.PlaceQueryModel
import com.kristinaefros.challenge.domain.places.PlacesInteractor
import com.kristinaefros.challenge.utils.extensions.Constants.locationChannelId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.component.KoinComponent
import org.koin.java.KoinJavaComponent.inject
import timber.log.Timber

class LocationService : Service(), KoinComponent {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val placesInteractor: PlacesInteractor by inject(PlacesInteractor::class.java)
    private lateinit var locationClient: PhotoLocationClient

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        locationClient = PhotoLocationClient(LocationServices.getFusedLocationProviderClient(applicationContext))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        val notification = NotificationCompat.Builder(this, locationChannelId)
            .setContentTitle(this.resources.getString(R.string.LOCATION_SERVICE_TITLE))
            .setContentText(this.resources.getString(R.string.LOCATION_SERVICE_MESSAGE))
            .setSmallIcon(R.drawable.ic_location_notification)
            .setOngoing(true)
            .build()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            startForeground(1003, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION)
        } else {
            startForeground(1003, notification)
        }

        locationClient
            .getLocationUpdates(10000L)
            .catch { error -> Timber.e(error) }
            .onEach { location ->
                val radius = location.accuracy / 100
                val query = PlaceQueryModel(location.latitude, location.longitude, radius)
                placesInteractor.createPlace(query)
            }
            .launchIn(serviceScope)
    }

    private fun stop() {
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}