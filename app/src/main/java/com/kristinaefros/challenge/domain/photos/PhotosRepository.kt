package com.kristinaefros.challenge.domain.photos

interface PhotosRepository {
    suspend fun search(latitude: Double, longitude: Double, radius: Float): PhotoModel?
}