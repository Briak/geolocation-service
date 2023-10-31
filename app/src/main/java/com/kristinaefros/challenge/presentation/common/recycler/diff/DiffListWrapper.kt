package com.kristinaefros.challenge.presentation.common.recycler.diff

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class DiffListWrapper(private val mAdapter: RecyclerView.Adapter<*>) {

    private val mData = mutableListOf<DiffEntity>()

    fun setData(data: List<DiffEntity>) {
        val diffResult = DiffUtil.calculateDiff(
            DiffCallback(
                mData,
                data
            )
        )
        mData.clear()
        mData.addAll(data)
        diffResult.dispatchUpdatesTo(mAdapter)
    }

    fun size(): Int {
        return mData.size
    }

    fun getAll(): List<DiffEntity> {
        return mData
    }

    fun get(position: Int): DiffEntity {
        return mData[position]
    }

    private class DiffCallback constructor(
        private val mOldData: List<DiffEntity>,
        private val mNewData: List<DiffEntity>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return mOldData.size
        }

        override fun getNewListSize(): Int {
            return mNewData.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return mOldData[oldItemPosition].areItemsTheSame(mNewData[newItemPosition])
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return mOldData[oldItemPosition].areContentsTheSame(mNewData[newItemPosition])
        }

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            return java.lang.Boolean.FALSE
        }

    }

}