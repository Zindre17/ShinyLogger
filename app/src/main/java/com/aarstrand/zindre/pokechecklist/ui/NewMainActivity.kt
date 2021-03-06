package com.aarstrand.zindre.pokechecklist.ui


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.aarstrand.zindre.pokechecklist.HuntActivity
import com.aarstrand.zindre.pokechecklist.MyShiniesActivity
import com.aarstrand.zindre.pokechecklist.R
import com.aarstrand.zindre.pokechecklist.viewmodels.Launch
import com.aarstrand.zindre.pokechecklist.viewmodels.MainViewModel
import com.aarstrand.zindre.pokechecklist.databinding.MainBinding
import com.aarstrand.zindre.pokechecklist.tools.PokedexCreator

class NewMainActivity: AppCompatActivity() {

    private val viewModel by lazy { ViewModelProviders.of(this).get(MainViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding : MainBinding = DataBindingUtil.setContentView(this, R.layout.main)

        val context = this
        binding.main = viewModel
        viewModel.launchEvent.observe(this, Observer {
            when (it){
                Launch.DEX -> context.startActivity(Intent(this, DexActivity::class.java))
                Launch.HUNT -> return@Observer
                Launch.COLL -> context.startActivity(Intent(this, MyShiniesActivity::class.java))
                Launch.NONE -> return@Observer
                Launch.PROG -> return@Observer
                else -> return@Observer
            }
        })

        val worker = OneTimeWorkRequestBuilder<PokedexCreator>().build()
        WorkManager.getInstance(this).enqueue(worker)
    }
}