package com.aarstrand.zindre.pokechecklist.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aarstrand.zindre.pokechecklist.R
import com.aarstrand.zindre.pokechecklist.RegisterActivity
import com.aarstrand.zindre.pokechecklist.custom.DexItem
import com.aarstrand.zindre.pokechecklist.data.Pokemon
import com.aarstrand.zindre.pokechecklist.ui.RegActivity

class DexAdapter : PagedListAdapter<Pokemon, DexAdapter.PokemonViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.dex_item, parent, false)
        return PokemonViewHolder(itemView)
    }

    inner class PokemonViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        private val dexItemView: DexItem = itemView.findViewById(R.id.item)

        fun bindTo(pokemon:Pokemon?){
            if(pokemon!=null){
                dexItemView.setName(pokemon.name)
                dexItemView.setNumber(pokemon.number)
                dexItemView.setImage(pokemon.image)
                dexItemView.setType1(pokemon.type1)
                dexItemView.setType2(pokemon.type2)
                dexItemView.setCount(pokemon.collected)
            }
        }

    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int){
        val pokemon: Pokemon? = getItem(position)
        holder.bindTo(pokemon)
        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, RegActivity::class.java)
            intent.putExtra(it.resources.getString(R.string.number),pokemon!!.number)
            it.context.startActivity(intent)
        }
    }

    companion object{
        private val DIFF_CALLBACK = object:
            DiffUtil.ItemCallback<Pokemon>() {

            override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
                return oldItem.number == newItem.number
            }

            override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
                return oldItem == newItem
            }
        }
    }
}