package com.kristinaefros.challenge.presentation.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean

abstract class BaseViewModel : ViewModel() {
    private val throttle = AtomicBoolean(false)

    private val managedJobList: MutableList<Job> = mutableListOf()
    private val localJobByUid: MutableMap<String, Job> = mutableMapOf()
    private val globalJobByUid: MutableMap<String, Job> = mutableMapOf()


    protected fun <T> Flow<T>.launch(): Job {
        return this
            .catch { error -> Timber.e(error) }
            .flowOn(Dispatchers.Default)
            .launchIn(viewModelScope)
    }

    protected fun Job.manageJob() {
        managedJobList.add(this)
    }

    protected fun disposeJobs() {
        managedJobList.toList().forEach { job -> job.cancel() }
        managedJobList.clear()

        localJobByUid.toMap().forEach { job -> job.value.cancel() }
        localJobByUid.clear()
    }

    protected fun launchBlocking(block: suspend () -> Unit) = launch {
        if (throttle.compareAndSet(false, true)) {
            try {
                block()
            } finally {
                throttle.set(false)
            }
        }
    }

    protected fun launch(uid: String? = null, block: suspend () -> Unit) {
        if (uid != null) {
            localJobByUid[uid]?.cancel()
            localJobByUid[uid] = launchJob(block)
        } else {
            launchJob(block)
        }
    }

    protected fun launchJob(block: suspend () -> Unit) = viewModelScope
        .launch(Dispatchers.Default) {
            try {
                block()
            } catch (error: Exception) {
                Timber.e(error)
            }
        }

    protected fun launchGlobal(uid: String? = null, block: suspend () -> Unit) {
        if (uid != null) {
            globalJobByUid[uid]?.cancel()
            globalJobByUid[uid] = launchGlobalJob(block)
        } else {
            launchGlobalJob(block)
        }
    }

    protected fun launchGlobalJob(block: suspend () -> Unit) =
        GlobalScope.launch(Dispatchers.Default) {
            try {
                block()
            } catch (error: Exception) {
                Timber.e(error)
            }
        }
}