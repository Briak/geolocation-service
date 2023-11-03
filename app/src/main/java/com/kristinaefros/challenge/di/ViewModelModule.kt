package com.kristinaefros.challenge.di

import com.kristinaefros.challenge.presentation.main.MainViewModel
import com.kristinaefros.challenge.presentation.start.StartViewModel
import com.kristinaefros.challenge.presentation.places.PlacesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object ViewModelModule {
    val module = module {
        viewModel { MainViewModel(get()) }
        viewModel { StartViewModel(get()) }
        viewModel { PlacesViewModel(get(), get()) }
    }
}