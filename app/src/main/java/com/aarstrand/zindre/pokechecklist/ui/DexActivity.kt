package com.aarstrand.zindre.pokechecklist.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.aarstrand.zindre.pokechecklist.R
import com.aarstrand.zindre.pokechecklist.adapters.DexAdapter
import com.aarstrand.zindre.pokechecklist.databinding.DexBinding
import com.aarstrand.zindre.pokechecklist.viewmodels.PokedexViewModel

class DexActivity: AppCompatActivity(){

    private val viewModel by lazy { ViewModelProviders.of(this).get(PokedexViewModel::class.java)}

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        val binding : DexBinding = DataBindingUtil.setContentView(this, R.layout.dex)

        binding.dexmodel = viewModel

        val adapter = DexAdapter()
        viewModel.pokemon.observe(this, Observer{
            adapter.submitList(it)
        })
        binding.dex.adapter = adapter

    }
}