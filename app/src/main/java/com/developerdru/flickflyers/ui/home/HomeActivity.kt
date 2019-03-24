package com.developerdru.flickflyers.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.developerdru.flickflyers.R
import dagger.android.AndroidInjection
import javax.inject.Inject

class HomeActivity : AppCompatActivity() {

    companion object {
        const val DEFAULT_SEARCH_TERM = "sunset"
    }

    @Inject
    lateinit var homeVMFactory: HomeVMFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val homeVM = ViewModelProviders.of(this, homeVMFactory).get(HomeVM::class.java)

        homeVM.getViewState().observe(this, Observer {
            when {
                it.loading -> println("loading")
                it.error != null -> println("ERROR in HomeActivity: ${it.error}")
                else -> println("${it.data}")
            }
        })

        homeVM.searchPhotoByTag(DEFAULT_SEARCH_TERM)
    }
}
