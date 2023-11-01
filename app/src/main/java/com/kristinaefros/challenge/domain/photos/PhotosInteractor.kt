package com.kristinaefros.challenge.domain.photos

class PhotosInteractor(
    private val photosRepository: PhotosRepository,
) {
    suspend fun searchPhoto(latitude: Double, longitude: Double): PhotoModel? = photosRepository.search(latitude, longitude)
}