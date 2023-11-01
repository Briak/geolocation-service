package com.kristinaefros.challenge.data.network.response

import com.google.gson.annotations.SerializedName
import com.kristinaefros.challenge.data.network.dto.PhotoDto

data class PhotosResponse(
    @SerializedName("photo") val photo: List<PhotoDto>,
)
