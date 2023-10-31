package com.kristinaefros.challenge.presentation.common.recycler.multi

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class MultiTypeManager {

    private val mBinderArray = SparseArray<MultiTypeViewBinder<RecyclerView.ViewHolder, Any>>()

    fun addBinder(type: Int, binder: MultiTypeViewBinder<RecyclerView.ViewHolder, Any>) {
        mBinderArray.put(type, binder)
    }

    fun getItemViewType(model: Any): Int {
        var i = 0
        val size = mBinderArray.size()
        while (i < size) {
            val delegate = mBinderArray.valueAt(i)
            if (delegate.isValidModel(model)) {
                return mBinderArray.keyAt(i)
            }
            i++
        }
        throw RuntimeException("unknown type " + model.javaClass.name)
    }

    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return getViewBinder(viewType).onCreateViewHolder(LayoutInflater.from(parent.context), parent)
    }

    fun onBindViewHolder(holder: RecyclerView.ViewHolder, model: Any) {
        getViewBinder(getItemViewType(model)).onBindViewHolder(holder, model)
    }

    private fun getViewBinder(viewType: Int): MultiTypeViewBinder<RecyclerView.ViewHolder, Any> {
        return mBinderArray[viewType]
    }
}