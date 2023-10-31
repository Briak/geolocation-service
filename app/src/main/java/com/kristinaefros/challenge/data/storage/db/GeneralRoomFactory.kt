package com.kristinaefros.challenge.data.storage.db

import android.content.Context
import androidx.room.Room


object GeneralRoomFactory {

    fun create(context: Context): GeneralRoom {
        return Room.databaseBuilder(context, GeneralRoom::class.java, "general.db")
            .fallbackToDestructiveMigration()
            .build()
    }
}