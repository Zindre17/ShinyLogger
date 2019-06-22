package com.aarstrand.zindre.pokechecklist.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aarstrand.zindre.pokechecklist.R
import com.aarstrand.zindre.pokechecklist.custom.DexItem
import com.aarstrand.zindre.pokechecklist.data.Pokemon

class DexAdapter internal constructor(
        context: Context
): RecyclerView.Adapter<DexAdapter.PokemonViewHolder>(){

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var pokemon = emptyList<Pokemon>()

    inner class PokemonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val dexItemView: DexItem = itemView.findViewById(R.id.item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val itemView = inflater.inflate(R.layout.dex_item, parent, false)
        return PokemonViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val current = pokemon[position]
        holder.dexItemView.setName(current.name)
        holder.dexItemView.setNumber(current.number)
        //holder.dexItemView.setImage(current.image)
        holder.dexItemView.setType1(current.type1)
        holder.dexItemView.setType2(current.type2)
        //holder.dexItemView.setCount()
    }

    internal fun setPokemon(pokemon: List<Pokemon>){
        this.pokemon = pokemon
        notifyDataSetChanged()
    }

    override fun getItemCount() = pokemon.size
}