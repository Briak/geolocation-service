package com.kristinaefros.challenge.data.network.response

import com.google.gson.annotations.SerializedName

data class SearchPhotosResponse(
    @SerializedName("photos") val photos: PhotosResponse,
)
