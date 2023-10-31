package com.kristinaefros.challenge.data.preference

import com.orhanobut.hawk.Hawk
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock


class NullablePreference<T>(
    private val key: String,
) {

    private val mutex = Mutex()
    private val sharedFlow = MutableSharedFlow<T?>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    fun observe(): Flow<T?> {
        return sharedFlow
            .onStart { emit(get()) }
            .distinctUntilChanged()
    }

    fun getOrThrow(): T {
        return loadInternal() ?: throw RuntimeException("value is null")
    }

    fun get(): T? {
        return loadInternal()
    }

    suspend fun remove() = put(null)

    suspend fun put(value: T?) = mutex.withLock {
        saveInternal(value)
        sharedFlow.emit(value)
    }

    suspend fun update(block: (T?) -> T) = mutex.withLock {
        val oldValue = loadInternal()
        val newValue = block(oldValue)
        saveInternal(newValue)
        sharedFlow.emit(newValue)
    }

    private fun loadInternal():  T? {
        return if (Hawk.contains(key)) {
            Hawk.get(key)
        } else {
            null
        }
    }

    private fun saveInternal(entity: T?) {
        if (entity == null) {
            Hawk.delete(key)
        } else {
            val success = Hawk.put(key, entity)
            if (success.not()) {
                throw RuntimeException("hawk failure")
            }
        }
    }
}