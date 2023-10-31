package com.kristinaefros.challenge.presentation.places

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kristinaefros.challenge.databinding.FragmentPlacesBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlacesFragment: Fragment() {
    private var _binding: FragmentPlacesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlacesViewModel by viewModel()

    companion object {
        fun newInstance() = PlacesFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPlacesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            stopButton.setOnClickListener { viewModel.stop() }
        }
    }

    override fun onDestroyView() {
        viewModel.apply {
//            screenData.removeObservers(viewLifecycleOwner)
        }
        _binding = null
        super.onDestroyView()
    }
}