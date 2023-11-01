package com.kristinaefros.challenge.data.network

import com.kristinaefros.challenge.data.network.response.SearchPhotosResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrApi {

    @GET("services/rest/")
    suspend fun searchPhotos(
        @Query("method") method: String = "flickr.photos.search",
        @Query("api_key") apiKey: String = "46643baf633721bb8e12ed2bfa23df11",
        @Query("accuracy") accuracy: Int = 16,
        @Query("content_types") contentTypes: Int = 0,
        @Query("per_page") perPage: Int = 1,
        @Query("format") format: String = "json",
        @Query("nojsoncallback") nojsoncallback: Int = 1,
        @Query("radius") radius: Int = 1,
//        @Query("geo_context") geoContext: Int = 2,
        @Query("lon") longitude: Double,
        @Query("lat") latitude: Double,
    ): SearchPhotosResponse
}