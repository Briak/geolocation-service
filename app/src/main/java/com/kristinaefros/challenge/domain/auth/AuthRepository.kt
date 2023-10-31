package com.kristinaefros.challenge.domain.auth

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun put(model: AuthModel)
    suspend fun get(): AuthModel?
    fun observe(): Flow<AuthModel?>
    suspend fun clear()
}