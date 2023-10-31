package com.kristinaefros.challenge.data.repository.places

import com.kristinaefros.challenge.data.storage.entity.PlaceEntity
import com.kristinaefros.challenge.domain.places.PlaceModel

object PlaceMapper {
    fun mapFromEntity(entity: PlaceEntity): PlaceModel =
        PlaceModel(
            id = entity.id,
            latitude = entity.latitude,
            longitude = entity.longitude,
            radius = entity.radius,
            photoUrl = entity.photoUrl,
        )
}