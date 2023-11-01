package com.kristinaefros.challenge.domain.photos

data class PhotoModel(
    val id: String,
    val owner: String,
    val secret: String,
    val server: String,
    val farm: Int,
    val title: String,
) {
    val url = "https://live.staticflickr.com/${server}/${id}_${secret}_w.jpg"
}
