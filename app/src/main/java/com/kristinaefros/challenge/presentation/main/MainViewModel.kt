package com.kristinaefros.challenge.presentation.main

import androidx.lifecycle.asLiveData
import com.kristinaefros.challenge.domain.auth.AuthInteractor
import com.kristinaefros.challenge.presentation.common.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class MainViewModel(
    private val authInteractor: AuthInteractor,
) : BaseViewModel() {

    private val stateFlow = MutableStateFlow(MainState())
    val screenData = stateFlow
        .map { it.authState }
        .asLiveData()

    fun subscribe() {
        authInteractor.observeSession()
            .onEach { authModel ->
                stateFlow.update { state -> state.copy(authModel = authModel) }
            }
            .launch()
            .manageJob()
    }

    fun unsubscribe() {
        disposeJobs()
    }
}