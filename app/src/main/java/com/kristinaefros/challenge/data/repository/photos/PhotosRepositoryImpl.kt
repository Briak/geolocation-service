package com.kristinaefros.challenge.data.repository.photos

import com.kristinaefros.challenge.data.network.FlickrApi
import com.kristinaefros.challenge.domain.photos.PhotoModel
import com.kristinaefros.challenge.domain.photos.PhotosRepository

class PhotosRepositoryImpl(
    private val flickrApi: FlickrApi,
): PhotosRepository {
    override suspend fun search(latitude: Double, longitude: Double, radius: Float): PhotoModel? {
        val photosResponse = flickrApi.searchPhotos(latitude = latitude, longitude = longitude, radius = radius)
        val dto = photosResponse.photos?.photo?.firstOrNull()
        return PhotoMapper.mapFromDto(dto)
    }
}