package com.kristinaefros.challenge.presentation.common.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kristinaefros.challenge.presentation.common.recycler.diff.DiffEntity
import com.kristinaefros.challenge.presentation.common.recycler.diff.DiffListWrapper
import com.kristinaefros.challenge.presentation.common.recycler.multi.MultiTypeManager
import com.kristinaefros.challenge.presentation.common.recycler.multi.MultiTypeViewBinder

abstract class MultiDiffSyncAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var attached: Boolean = false
    private val data: DiffListWrapper = DiffListWrapper(this)
    private val multiTypeManager = MultiTypeManager()

    @Suppress("UNCHECKED_CAST")
    fun addBinder(type: Int, binder: Any) {
        multiTypeManager.addBinder(type, binder as MultiTypeViewBinder<RecyclerView.ViewHolder, Any>)
    }

    fun getData(): List<DiffEntity> {
        return data.getAll()
    }

    fun setData(list: List<DiffEntity>) {
        data.setData(list)
    }

    open fun getItem(position: Int): DiffEntity {
        return data.get(position)
    }

    override fun getItemCount(): Int {
        return data.size()
    }

    override fun getItemViewType(position: Int): Int {
        val model: Any = getItem(position)
        return multiTypeManager.getItemViewType(model)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        if (attached) throw RuntimeException("recycler is already attached")
        recyclerView.post { preloadViewHolders(recyclerView) }
        attached = true
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        attached = false
    }

    open fun preloadViewHolders(recyclerView: RecyclerView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return multiTypeManager.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        multiTypeManager.onBindViewHolder(holder, getItem(position))
    }
}