package com.naldana.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.naldana.myapplication.data.entity.Pokemon

class PokemonPageAdapter(diffCallback: DiffUtil.ItemCallback<Pokemon>) :
    PagingDataAdapter<Pokemon, PokemonPageAdapter.PokemonViewHolder>(diffCallback) {

    class PokemonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val id = itemView.findViewById<TextView>(R.id.pokemon_id_text)
        val name = itemView.findViewById<TextView>(R.id.pokemon_name_text)

        fun bind(pokemon: Pokemon) {
            id.text = pokemon.id.toString()
            name.text = pokemon.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.pokemon_item, parent, false)
        return PokemonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = getItem(position)
        if (pokemon != null) {
            holder.bind(pokemon)
        }
    }
}