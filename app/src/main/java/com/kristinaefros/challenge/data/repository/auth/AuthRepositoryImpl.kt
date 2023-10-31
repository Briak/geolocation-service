package com.kristinaefros.challenge.data.repository.auth

import com.kristinaefros.challenge.data.preference.AppPreferences
import com.kristinaefros.challenge.domain.auth.AuthModel
import com.kristinaefros.challenge.domain.auth.AuthRepository
import kotlinx.coroutines.flow.Flow

class AuthRepositoryImpl(
    private val appPreferences: AppPreferences,
): AuthRepository {

    override suspend fun put(model: AuthModel) = appPreferences.authStorage().put(model)

    override suspend fun get(): AuthModel? = appPreferences.authStorage().get()

    override fun observe(): Flow<AuthModel?> = appPreferences.authStorage().observe()

    override suspend fun clear() = appPreferences.authStorage().remove()
}