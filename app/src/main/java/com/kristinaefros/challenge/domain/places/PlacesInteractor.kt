package com.kristinaefros.challenge.domain.places

import kotlinx.coroutines.flow.Flow

class PlacesInteractor(
    private val placesRepository: PlacesRepository,
) {
    fun observePlaces(): Flow<List<PlaceModel>> = placesRepository.observe()
    suspend fun createPlace(latitude: Double, longitude: Double, radius: Double) = placesRepository.create(latitude, longitude, radius)
    suspend fun addPhotoToPlace(placeId: Int, url: String) = placesRepository.addPhoto(placeId, url)
    suspend fun clearPlaces() = placesRepository.clear()
}