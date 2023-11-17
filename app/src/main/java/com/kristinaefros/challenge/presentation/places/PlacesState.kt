package com.kristinaefros.challenge.presentation.places

import androidx.annotation.StringRes
import com.kristinaefros.challenge.R
import com.kristinaefros.challenge.domain.places.PlaceModel
import com.kristinaefros.challenge.presentation.places.binder.PlaceUiModel

data class PlacesState(
    val loading: Boolean = false,
    val placeModels: List<PlaceModel> = emptyList(),
    val showPermissionError: Boolean = false,
    val showGpsError: Boolean = false,
) {
    val screenUiModel = ScreenUiModel(loading, placeModels, showPermissionError, showGpsError)
}

data class ScreenUiModel(
    val loading: Boolean = false,
    val placeModels: List<PlaceModel> = emptyList(),
    val showPermissionError: Boolean = false,
    val showGpsError: Boolean = false,
) {
    val placeUiModels = placeModels
        .filter { place -> place.photoUrl != null }
        .map { place -> PlaceUiModel(place) }
        .sortedBy { place -> place.id }

    val errorAvailable = showPermissionError || showGpsError
    @StringRes val error = when {
        showPermissionError -> R.string.PLACES_SCREEN_GRANT_LOCATION_PERMISSION_MESSAGE
        showGpsError -> R.string.PLACES_SCREEN_ENABLE_GPS_MESSAGE
        else -> null
    }
}