package com.kristinaefros.challenge.domain.places

import com.kristinaefros.challenge.domain.photos.PhotosInteractor
import kotlinx.coroutines.flow.Flow

class PlacesInteractor(
    private val placesRepository: PlacesRepository,
    private val photosInteractor: PhotosInteractor,
) {
    fun observePlaces(): Flow<List<PlaceModel>> = placesRepository.observe()
    suspend fun createPlace(query: PlaceQueryModel) {
        val place = placesRepository.create(query)
        val photo = photosInteractor.searchPhoto(place.latitude, place.longitude, place.radius)
        if (photo != null) {
            addPhotoToPlace(place.id, photo.url)
        }
    }
    suspend fun addPhotoToPlace(placeId: Int, url: String) = placesRepository.addPhoto(placeId, url)
    suspend fun clearPlaces() = placesRepository.clear()
}