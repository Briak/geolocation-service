package com.kristinaefros.challenge.presentation.main

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import com.kristinaefros.challenge.databinding.ActivityMainBinding
import com.kristinaefros.challenge.presentation.common.navigation.Navigator
import com.kristinaefros.challenge.presentation.location_service.LocationService
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModel()
    private lateinit var navigator: Navigator

    private lateinit var locationService: LocationService
    private val locationServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as LocationService.LocationBinder
            locationService = binder.getService()
            locationService.userLocationEvent.observe(this@MainActivity) { placeQuery ->
                viewModel.createPlace(placeQuery)
            }
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
//            locationService.userLocationEvent.removeObservers(this@MainActivity)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navigator = Navigator(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.apply {
            subscribe()
        }
    }

    override fun onStart() {
        super.onStart()

        val intentBind = Intent(this, LocationService::class.java)
        bindService(intentBind, locationServiceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onResume() {
        super.onResume()
        viewModel.apply {
            screenData.observe(this@MainActivity) { state -> render(state) }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.apply {
            screenData.removeObservers(this@MainActivity)
        }
    }

    override fun onStop() {
        super.onStop()

        unbindService(locationServiceConnection)
    }

    override fun onDestroy() {
        viewModel.apply {
            unsubscribe()
        }
        super.onDestroy()
    }

    private fun render(state: AuthStateModel) {
        when (state) {
            is AuthStateModel.Authorized -> navigator.openPlacesScreen()
            else -> navigator.openStartScreen()
        }
    }
}