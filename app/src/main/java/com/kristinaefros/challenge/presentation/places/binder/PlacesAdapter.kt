package com.kristinaefros.challenge.presentation.places.binder

import com.kristinaefros.challenge.presentation.common.recycler.MultiDiffSyncAdapter

class PlacesAdapter : MultiDiffSyncAdapter() {

    init {
        addBinder(1, PlaceBinder())
    }
}