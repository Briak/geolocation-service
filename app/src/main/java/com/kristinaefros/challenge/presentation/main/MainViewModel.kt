package com.kristinaefros.challenge.presentation.main

import com.kristinaefros.challenge.presentation.common.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import androidx.lifecycle.asLiveData
import com.kristinaefros.challenge.domain.auth.AuthInteractor
import com.kristinaefros.challenge.domain.places.PlaceQueryModel
import com.kristinaefros.challenge.domain.places.PlacesInteractor
import com.kristinaefros.challenge.presentation.common.SingleLiveEvent
import com.kristinaefros.challenge.presentation.common.message.MessageEntity
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import timber.log.Timber
import java.lang.Exception

class MainViewModel(
    private val authInteractor: AuthInteractor,
    private val placesInteractor: PlacesInteractor,
) : BaseViewModel() {

    private val stateFlow = MutableStateFlow(MainState())
    val screenData = stateFlow
        .map { it.authState }
        .asLiveData()

    val messageEvent = SingleLiveEvent<MessageEntity>()

    fun subscribe() {
        authInteractor.observeSession()
            .onEach { authModel ->
                stateFlow.update { state -> state.copy(authModel = authModel) }
            }
            .launch()
            .manageJob()
    }

    fun createPlace(query: PlaceQueryModel) = launch {
        try {
            placesInteractor.createPlace(query)
        } catch (error: Exception) {
            Timber.e(error)
        }
    }

    fun unsubscribe() {
        disposeJobs()
    }
}