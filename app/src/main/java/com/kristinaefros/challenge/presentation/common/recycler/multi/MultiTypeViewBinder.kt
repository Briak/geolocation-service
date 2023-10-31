package com.kristinaefros.challenge.presentation.common.recycler.multi

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

interface MultiTypeViewBinder<Holder : RecyclerView.ViewHolder, Model : Any> {
    fun isValidModel(uiModel: Any): Boolean
    fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): Holder
    fun onBindViewHolder(holder: Holder, uiModel: Model)
}