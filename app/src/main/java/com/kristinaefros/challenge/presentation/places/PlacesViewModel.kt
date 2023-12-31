package com.kristinaefros.challenge.presentation.places

import androidx.lifecycle.asLiveData
import com.kristinaefros.challenge.domain.auth.AuthInteractor
import com.kristinaefros.challenge.domain.places.PlacesInteractor
import com.kristinaefros.challenge.presentation.common.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import timber.log.Timber

class PlacesViewModel(
    private val authInteractor: AuthInteractor,
    private val placesInteractor: PlacesInteractor,
): BaseViewModel() {

    private val stateFlow = MutableStateFlow(PlacesState())
    val screenData = stateFlow
        .map { it.screenUiModel }
        .asLiveData()

    fun subscribe() {
        placesInteractor.observePlaces()
            .onEach { places ->
                stateFlow.update { state -> state.copy(placeModels = places) }
            }
            .launch()
            .manageJob()
    }

    fun unsubscribe() {
        disposeJobs()
    }

    fun updatePermissionErrorState(show: Boolean) =
        stateFlow.update { state -> state.copy(showPermissionError = show) }

    fun updateGpsErrorState(show: Boolean) = stateFlow.update { state -> state.copy(showGpsError = show) }

    fun stop() = launch {
        try {
            authInteractor.stopSession()
        } catch (error: Exception) {
            Timber.e(error)
        }
    }
}