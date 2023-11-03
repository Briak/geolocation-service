package com.kristinaefros.challenge

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.kristinaefros.challenge.di.CommonModule
import com.kristinaefros.challenge.di.DataModule
import com.kristinaefros.challenge.di.DomainModule
import com.kristinaefros.challenge.di.NetworkModule
import com.kristinaefros.challenge.di.ViewModelModule
import com.kristinaefros.challenge.utils.extensions.Constants.locationChannelId
import com.kristinaefros.challenge.utils.extensions.Constants.locationChannelName
import com.orhanobut.hawk.Hawk
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber


class ChallengeApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initNotificationChannel()
        initHawk()
        setupLogging()
        initKoin()
    }

    private fun initNotificationChannel() {
        val channel = NotificationChannel(
            locationChannelId,
            locationChannelName,
            NotificationManager.IMPORTANCE_LOW,
        )
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun setupLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initHawk() {
        Hawk.init(this).build()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@ChallengeApplication)
            modules(
                NetworkModule.module,
                CommonModule.module,
                DataModule.module,
                DomainModule.module,
                ViewModelModule.module,
            )
        }
    }
}