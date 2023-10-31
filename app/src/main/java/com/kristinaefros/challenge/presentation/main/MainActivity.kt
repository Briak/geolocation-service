package com.kristinaefros.challenge.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kristinaefros.challenge.databinding.ActivityMainBinding
import com.kristinaefros.challenge.presentation.common.navigation.Navigator
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModel()
    private lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navigator = Navigator(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.apply {
            subscribe()
        }
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