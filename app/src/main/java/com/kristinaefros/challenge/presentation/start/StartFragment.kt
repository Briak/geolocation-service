package com.kristinaefros.challenge.presentation.start

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kristinaefros.challenge.databinding.FragmentStartBinding
import com.kristinaefros.challenge.presentation.location_service.LocationService
import com.kristinaefros.challenge.presentation.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class StartFragment : Fragment() {
    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!

    private val viewModel: StartViewModel by viewModel()

    companion object {
        fun newInstance() = StartFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            startButton.setOnClickListener {
                Intent(requireContext().applicationContext, LocationService::class.java).apply {
                    action = LocationService.ACTION_START
                    requireActivity().startService(this)
                }
//                (requireActivity() as MainActivity).startLocationService()
                viewModel.start()
            }
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