package com.kristinaefros.challenge

import android.app.Application
import com.kristinaefros.challenge.di.CommonModule
import com.kristinaefros.challenge.di.DataModule
import com.kristinaefros.challenge.di.DomainModule
import com.kristinaefros.challenge.di.ViewModelModule
import com.orhanobut.hawk.Hawk
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber


class ChallengeApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initHawk()
        setupLogging()
        initKoin()
    }

    private fun setupLogging() {
//        if (BuildConfig.DEBUG) {
//            Timber.plant(Timber.DebugTree())
//        }
    }

    private fun initHawk() {
        Hawk.init(this).build()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@ChallengeApplication)
            modules(
                CommonModule.module,
                DataModule.module,
                DomainModule.module,
                ViewModelModule.module,
            )
        }
    }
}