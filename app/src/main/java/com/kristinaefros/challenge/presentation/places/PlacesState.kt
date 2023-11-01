package com.kristinaefros.challenge.presentation.places

import com.kristinaefros.challenge.domain.places.PlaceModel
import com.kristinaefros.challenge.presentation.places.binder.PlaceUiModel

data class PlacesState(
    val loading: Boolean = false,
    val placeModels: List<PlaceModel> = emptyList(),
) {
    val screenUiModel = ScreenUiModel(loading, placeModels)
}

data class ScreenUiModel(
    val loading: Boolean = false,
    val placeModels: List<PlaceModel> = emptyList(),
) {
    val placeUiModels = placeModels
        .filter { place -> place.photoUrl != null }
        .map { place -> PlaceUiModel(place) }
        .sortedBy { place -> place.id }
}