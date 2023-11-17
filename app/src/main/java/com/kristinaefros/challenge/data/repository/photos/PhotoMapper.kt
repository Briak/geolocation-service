package com.kristinaefros.challenge.data.repository.photos

import com.kristinaefros.challenge.data.network.dto.PhotoDto
import com.kristinaefros.challenge.domain.photos.PhotoModel

object PhotoMapper {
    fun mapFromDto(dto: PhotoDto?): PhotoModel? {
        if (dto == null) return null
        return PhotoModel(
            id = dto.id,
            owner = dto.owner,
            secret = dto.secret,
            server = dto.server,
            farm = dto.farm,
            title = dto.title,
        )
    }
}