package com.kristinaefros.challenge.presentation.places.binder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kristinaefros.challenge.databinding.ItemPlaceBinding
import com.kristinaefros.challenge.domain.places.PlaceModel
import com.kristinaefros.challenge.presentation.common.recycler.diff.DiffEntity
import com.kristinaefros.challenge.presentation.common.recycler.multi.MultiTypeViewBinder
import com.kristinaefros.challenge.utils.extensions.setImageUri

data class PlaceUiModel(
    val model: PlaceModel,
) : DiffEntity {

    override fun areItemsTheSame(entity: DiffEntity): Boolean = entity is PlaceUiModel && entity.id == id
    override fun areContentsTheSame(entity: DiffEntity): Boolean = equals(entity)

    val id = model.id
    val photoUrl = model.photoUrl
}

class PlaceBinder : MultiTypeViewBinder<PlaceBinder.ViewHolder, PlaceUiModel> {

    override fun isValidModel(uiModel: Any): Boolean {
        return uiModel is PlaceUiModel
    }

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        val binding = ItemPlaceBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, uiModel: PlaceUiModel) {
        holder.binding.apply {
            placeImage.setImageUri(uiModel.photoUrl)
            placeId.text = uiModel.id.toString()
        }
    }

    class ViewHolder(
        val binding: ItemPlaceBinding,
    ) : RecyclerView.ViewHolder(binding.root)

}