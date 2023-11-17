package com.kristinaefros.challenge.di

import com.kristinaefros.challenge.domain.auth.AuthInteractor
import com.kristinaefros.challenge.domain.photos.PhotosInteractor
import com.kristinaefros.challenge.domain.places.PlacesInteractor
import org.koin.dsl.module

object DomainModule {

    val module = module {
        single { AuthInteractor(get(), get()) }
        single { PlacesInteractor(get(), get()) }
        single { PhotosInteractor(get()) }
    }
}