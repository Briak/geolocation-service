package com.kristinaefros.challenge.presentation.places

import android.Manifest
import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kristinaefros.challenge.R
import com.kristinaefros.challenge.databinding.FragmentPlacesBinding
import com.kristinaefros.challenge.presentation.location_service.LocationService
import com.kristinaefros.challenge.presentation.places.binder.PlacesAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlacesFragment : Fragment() {
    private var _binding: FragmentPlacesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlacesViewModel by viewModel()
    private val placesAdapter = PlacesAdapter()

    private val actionGPS = "android.location.PROVIDERS_CHANGED"
    private var geoLocationReceiver: BroadcastReceiver? = null

    private lateinit var locationRationaleDialog: AlertDialog
    private val requiredLocationPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    private val requestLocationLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION) || isPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                startLocationService()
            } else {
                startLocationServiceWithAction(LocationService.ACTION_STOP)
                viewModel.updatePermissionErrorState(true)
            }
        }

    companion object {
        fun newInstance() = PlacesFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPlacesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationRationaleDialog = requireActivity().let {
            val builder = MaterialAlertDialogBuilder(requireActivity())
            builder.apply {
                setTitle(R.string.PLACES_SCREEN_GRANT_LOCATION_PERMISSION_TITLE)
                setMessage(R.string.PLACES_SCREEN_GRANT_LOCATION_PERMISSION_MESSAGE)
                setCancelable(false)
                setPositiveButton(R.string.GENERAL_OK) { dialog, _ ->
                    requestLocationPermissions()
                    dialog.dismiss()
                }
                setNegativeButton(R.string.GENERAL_NO) { dialog, _ ->
                    viewModel.updatePermissionErrorState(true)
                    dialog.dismiss()
                }
            }
            builder.create()
        }

        viewModel.subscribe()

        binding.apply {
            stopButton.setOnClickListener { viewModel.stop() }

            placesList.layoutManager = LinearLayoutManager(requireActivity()).apply {
                stackFromEnd = true
                reverseLayout = true
            }
            placesList.adapter = placesAdapter
        }

        viewModel.screenData.observe(viewLifecycleOwner) { render(it) }
    }

    override fun onResume() {
        super.onResume()
        checkLocationPermissions()
        registerGPSReceiver()
    }

    override fun onPause() {
        geoLocationReceiver?.let { requireActivity().unregisterReceiver(it) }
        super.onPause()
    }

    override fun onDestroyView() {
        viewModel.apply {
            screenData.removeObservers(viewLifecycleOwner)
            unsubscribe()
        }
        binding.placesList.adapter = null
        _binding = null
        super.onDestroyView()
    }

    private fun render(screenUiModel: ScreenUiModel) {
        placesAdapter.setData(screenUiModel.placeUiModels)
        binding.apply {
            errorMessage.isVisible = screenUiModel.errorAvailable
            screenUiModel.error?.let { error -> errorMessage.setText(error) }
            if (placesList.scrollState == RecyclerView.SCROLL_STATE_IDLE) {
                placesList.smoothScrollToPosition((placesAdapter.itemCount - 1).coerceAtLeast(0))
            }
        }
    }

    private fun checkLocationPermissions() {
        when {
            isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION) -> startLocationService()
            isPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION) -> startLocationService()
            requireActivity().shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) ||
                    requireActivity().shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> showLocationRationale()
            else -> requestLocationPermissions()
        }
    }

    private fun checkGeoLocationEnabled() {
        val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        viewModel.updateGpsErrorState(isGpsEnabled.not())
    }

    private fun startLocationService() {
        checkGeoLocationEnabled()
        viewModel.updatePermissionErrorState(false)

        if (isLocationServiceRunning().not()) {
            startLocationServiceWithAction(LocationService.ACTION_START)
        }
    }

    private fun showLocationRationale() = locationRationaleDialog.show()

    private fun requestLocationPermissions() = requestLocationLauncher.launch(requiredLocationPermissions)

    private fun isPermissionGranted(permission: String) =
        ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED
    private fun isLocationServiceRunning(): Boolean {
        val manager = requireActivity().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (LocationService::class.java.name.equals(service.service.className)) {
                return service.foreground
            }
        }
        return false
    }

    private fun registerGPSReceiver() {
        val filter = IntentFilter()
        filter.addAction(actionGPS)
        if (geoLocationReceiver == null) {
            geoLocationReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent?) {
                    if (intent != null) {
                        val action = intent.action
                        if (action != null) {
                            if (action == actionGPS) checkGeoLocationEnabled()
                        }
                    }
                }
            }
        }
        ContextCompat.registerReceiver(requireContext(), geoLocationReceiver, filter, ContextCompat.RECEIVER_NOT_EXPORTED)
    }

    private fun startLocationServiceWithAction(serviceAction: String) {
        Intent(requireActivity().applicationContext, LocationService::class.java).apply {
            action = serviceAction
            requireActivity().startService(this)
        }
    }
}