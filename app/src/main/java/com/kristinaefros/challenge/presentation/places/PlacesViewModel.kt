package com.kristinaefros.challenge.presentation.places

import com.kristinaefros.challenge.domain.auth.AuthInteractor
import com.kristinaefros.challenge.domain.places.PlacesInteractor
import com.kristinaefros.challenge.presentation.common.BaseViewModel
import kotlinx.coroutines.flow.onEach

class PlacesViewModel(
    private val authInteractor: AuthInteractor,
    private val placesInteractor: PlacesInteractor,
): BaseViewModel() {

    fun subscribe() {
        placesInteractor.observePlaces()
            .onEach {  }
            .launch()
            .manageJob()
    }

    fun unsubscribe() {
        disposeJobs()
    }

    fun stop() = launch {
        try {
            authInteractor.stopSession()
        } catch (error: Exception) {

        }
    }
}