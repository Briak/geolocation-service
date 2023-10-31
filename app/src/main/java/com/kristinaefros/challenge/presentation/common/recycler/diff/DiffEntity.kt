package com.kristinaefros.challenge.presentation.common.recycler.diff


interface DiffEntity {
    fun areItemsTheSame(entity: DiffEntity): Boolean
    fun areContentsTheSame(entity: DiffEntity): Boolean
}