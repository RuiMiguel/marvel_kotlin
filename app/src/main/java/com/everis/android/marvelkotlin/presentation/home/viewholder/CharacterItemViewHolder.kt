package com.everis.android.marvelkotlin.presentation.home.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.everis.android.marvelkotlin.R
import com.everis.android.marvelkotlin.databinding.ItemCharacterBinding
import com.everis.android.marvelkotlin.domain.model.Character
import kotlinx.android.extensions.LayoutContainer

class CharacterItemViewHolder(
    override val containerView: View,
    private val itemClick: (Character) -> Unit
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    private val notAvailable = "image_not_available"

    fun bindTo(value: Character, position: Int) {
        val binding = ItemCharacterBinding.bind(containerView)

        binding.apply {
            val imageUrl = "${value.thumbnail.path}/standard_medium.${value.thumbnail.extension}"
            if (imageUrl.contains(notAvailable)) {
                image.load(R.drawable.placeholder)
            } else {
                image.load(imageUrl) {
                    crossfade(true)
                    placeholder(R.drawable.placeholder)
                }
            }

            name.text = value.name
        }
        itemView.setOnClickListener { itemClick(value) }
    }
}