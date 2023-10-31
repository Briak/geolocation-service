package com.kristinaefros.challenge.data.storage.dao

import androidx.room.*
import com.kristinaefros.challenge.data.storage.entity.PlaceEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PlaceDao {

    @Query("SELECT * FROM places")
    abstract fun observeAll(): Flow<List<PlaceEntity>>

    suspend fun create(latitude: Double, longitude: Double, radius: Double): PlaceEntity {
        val entity = PlaceEntity(latitude, longitude, radius)
        putInternal(entity)
        return entity
    }

    suspend fun updatePhoto(id: Int, photoUrl: String) {
        val currentPlace = getInternal(id)?.copy(photoUrl = photoUrl)
        currentPlace?.let { putInternal(it) }
    }

    suspend fun clear() {
        deleteAll()
    }

    @Query("SELECT * FROM places WHERE id = :id")
    abstract suspend fun getInternal(id: Int): PlaceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun putInternal(entity: PlaceEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun putAll(entity: List<PlaceEntity>)

    @Query("DELETE FROM places")
    protected abstract suspend fun deleteAll()

}