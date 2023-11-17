package com.kristinaefros.challenge.domain.places

data class PlaceQueryModel(
    val latitude: Double,
    val longitude: Double,
    val radius: Float,
)
