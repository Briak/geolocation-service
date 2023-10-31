package com.kristinaefros.challenge.data.storage

import android.content.Context
import com.kristinaefros.challenge.data.storage.db.GeneralRoom
import com.kristinaefros.challenge.data.storage.db.GeneralRoomFactory

class AppStorage(context: Context) {

    private val room: GeneralRoom = GeneralRoomFactory.create(context)

    val placeDao = room.placeDao()

    fun clear() {
        room.clearAllTables()
    }
}