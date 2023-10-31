package com.kristinaefros.challenge.presentation.start

import com.kristinaefros.challenge.domain.auth.AuthInteractor
import com.kristinaefros.challenge.domain.auth.AuthModel
import com.kristinaefros.challenge.presentation.common.BaseViewModel

class StartViewModel(
    private val authInteractor: AuthInteractor,
): BaseViewModel() {

    fun start() = launch {
        try {
            authInteractor.startSession(AuthModel("random string"))
        } catch (error: Exception) {

        }
    }
}