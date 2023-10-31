package com.kristinaefros.challenge.data.storage.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kristinaefros.challenge.data.storage.dao.PlaceDao
import com.kristinaefros.challenge.data.storage.entity.PlaceEntity


@Database(
    entities = [
        PlaceEntity::class,
    ],
    version = 1,
    exportSchema = false
)

//@TypeConverters(GeneralConverters::class)
abstract class GeneralRoom : RoomDatabase() {
    abstract fun placeDao(): PlaceDao
}