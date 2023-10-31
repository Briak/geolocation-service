package com.kristinaefros.challenge.domain.auth

import com.kristinaefros.challenge.domain.places.PlacesInteractor
import kotlinx.coroutines.flow.Flow

class AuthInteractor(
    private val authRepository: AuthRepository,
    private val placesInteractor: PlacesInteractor,
) {
    suspend fun startSession(model: AuthModel) = authRepository.put(model)
    suspend fun getSession(): AuthModel? = authRepository.get()
    fun observeSession(): Flow<AuthModel?> = authRepository.observe()
    suspend fun stopSession() {
        placesInteractor.clearPlaces()
        authRepository.clear()
    }
}