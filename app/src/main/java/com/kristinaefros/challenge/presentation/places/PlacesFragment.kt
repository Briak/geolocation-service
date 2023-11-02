package com.kristinaefros.challenge.presentation.places

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.kristinaefros.challenge.databinding.FragmentPlacesBinding
import com.kristinaefros.challenge.presentation.location_service.LocationService
import com.kristinaefros.challenge.presentation.main.MainActivity
import com.kristinaefros.challenge.presentation.places.binder.PlacesAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlacesFragment: Fragment() {
    private var _binding: FragmentPlacesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlacesViewModel by viewModel()
    private val placesAdapter = PlacesAdapter()

    companion object {
        fun newInstance() = PlacesFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPlacesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
    }
}