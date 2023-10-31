package com.kristinaefros.challenge.presentation.common.recycler.diff

import androidx.recyclerview.widget.DiffUtil


class DiffCallback : DiffUtil.ItemCallback<DiffEntity>() {

    override fun areItemsTheSame(oldItem: DiffEntity, newItem: DiffEntity): Boolean {
        return oldItem.areItemsTheSame(newItem)
    }

    override fun areContentsTheSame(oldItem: DiffEntity, newItem: DiffEntity): Boolean {
        return oldItem.areContentsTheSame(newItem)
    }

    override fun getChangePayload(oldItem: DiffEntity, newItem: DiffEntity): Any {
        return true
    }
}