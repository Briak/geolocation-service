package com.kristinaefros.challenge.domain.places

data class PlaceModel(
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val radius: Float,
    val photoUrl: String?,
)
