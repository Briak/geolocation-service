package com.kristinaefros.challenge.di

import android.app.NotificationManager
import android.content.Context
import com.google.gson.GsonBuilder
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


object CommonModule {
    val module = module {
        single {
            val gsonBuilder = GsonBuilder()
            val gson = gsonBuilder
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSZ")
                .create()
            gson
        }
        single { androidContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }
    }
}