package com.kristinaefros.challenge.domain.places

import kotlinx.coroutines.flow.Flow

interface PlacesRepository {
    fun observe(): Flow<List<PlaceModel>>
    suspend fun create(latitude: Double, longitude: Double, radius: Double): PlaceModel
    suspend fun addPhoto(placeId: Int, url: String)
    suspend fun clear()
}