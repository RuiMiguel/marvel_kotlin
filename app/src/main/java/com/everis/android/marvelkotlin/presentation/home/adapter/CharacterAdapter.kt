package com.everis.android.marvelkotlin.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.everis.android.marvelkotlin.R
import com.everis.android.marvelkotlin.domain.model.Character
import com.everis.android.marvelkotlin.presentation.home.viewholder.CharacterItemViewHolder

class CharacterAdapter(
    private val itemClick: (Character) -> Unit
) : ListAdapter<Character, CharacterItemViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_character, parent, false)
        return CharacterItemViewHolder(view, itemClick)
    }

    override fun onBindViewHolder(holder: CharacterItemViewHolder, position: Int) {
        holder.bindTo(getItem(position), position)
    }

    companion object {
        val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<Character>() {
                override fun areItemsTheSame(
                    oldItem: Character,
                    newItem: Character,
                ): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(
                    oldItem: Character,
                    newItem: Character,
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}