package com.kristinaefros.challenge.presentation.places

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
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
            permissionError.isVisible = screenUiModel.showPermissionError
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

    private fun startLocationService() {
        viewModel.updatePermissionErrorState(false)
        startLocationServiceWithAction(LocationService.ACTION_START)
    }

    private fun showLocationRationale() = locationRationaleDialog.show()

    private fun requestLocationPermissions() = requestLocationLauncher.launch(requiredLocationPermissions)

    private fun isPermissionGranted(permission: String) =
        ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED

    private fun startLocationServiceWithAction(serviceAction: String) {
        Intent(requireActivity().applicationContext, LocationService::class.java).apply {
            action = serviceAction
            requireActivity().startService(this)
        }
    }
}