package com.aarstrand.zindre.pokechecklist.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.aarstrand.zindre.pokechecklist.R
import com.aarstrand.zindre.pokechecklist.databinding.RegisterBinding
import com.aarstrand.zindre.pokechecklist.viewmodels.RegisterViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RegActivity: AppCompatActivity(){

    private val viewModel by lazy { ViewModelProviders.of(this).get(RegisterViewModel::class.java)}

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        val binding : RegisterBinding = DataBindingUtil.setContentView(this, R.layout.register)

        binding.register = viewModel


        viewModel.setNumber(intent.getIntExtra(getString(R.string.number),0))


    }
}