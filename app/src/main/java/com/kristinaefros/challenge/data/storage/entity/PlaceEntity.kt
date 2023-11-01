package com.kristinaefros.challenge.data.storage.entity

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(
    tableName = "places"
)
data class PlaceEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val latitude: Double,
    val longitude: Double,
    val radius: Float,
    val photoUrl: String?,
) {
    constructor(latitude: Double, longitude: Double, radius: Float) : this(0, latitude, longitude, radius, null)
}
