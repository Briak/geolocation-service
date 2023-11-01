package com.kristinaefros.challenge.data.repository.places

import com.kristinaefros.challenge.data.network.FlickrApi
import com.kristinaefros.challenge.data.storage.AppStorage
import com.kristinaefros.challenge.domain.places.PlaceModel
import com.kristinaefros.challenge.domain.places.PlaceQueryModel
import com.kristinaefros.challenge.domain.places.PlacesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapLatest

class PlacesRepositoryImpl(
    private val storage: AppStorage,
) : PlacesRepository {
    override fun observe(): Flow<List<PlaceModel>> =
        storage.placeDao.observeAll()
            .mapLatest { entities ->
                entities.map { entity ->
                    PlaceMapper.mapFromEntity(entity)
                }
            }
            .distinctUntilChanged()

    override suspend fun create(query: PlaceQueryModel): PlaceModel {
        val entity = storage.placeDao.create(query.latitude, query.longitude, query.radius)
        return PlaceMapper.mapFromEntity(entity)
    }

    override suspend fun addPhoto(placeId: Int, url: String) = storage.placeDao.updatePhoto(placeId, url)

    override suspend fun clear() = storage.placeDao.clear()
}