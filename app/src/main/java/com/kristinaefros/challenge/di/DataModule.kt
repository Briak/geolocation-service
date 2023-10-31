package com.kristinaefros.challenge.di


import com.kristinaefros.challenge.data.preference.AppPreferences
import com.kristinaefros.challenge.data.repository.auth.AuthRepositoryImpl
import com.kristinaefros.challenge.data.repository.places.PlacesRepositoryImpl
import com.kristinaefros.challenge.data.storage.AppStorage
import com.kristinaefros.challenge.domain.auth.AuthRepository
import com.kristinaefros.challenge.domain.places.PlacesRepository
import org.koin.dsl.module

object DataModule {

    val module = module {
        single { AppPreferences() }
        single { AppStorage(get()) }
        single<AuthRepository> { AuthRepositoryImpl(get()) }
        single<PlacesRepository> { PlacesRepositoryImpl(get()) }
    }
}