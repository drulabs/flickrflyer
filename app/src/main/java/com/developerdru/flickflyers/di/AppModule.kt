package com.developerdru.flickflyers.di

import android.app.Application
import android.content.Context
import com.developerdru.flickflyers.data.PhotoRepository
import com.developerdru.flickflyers.data.PhotoRepositoryImpl
import com.developerdru.flickflyers.ui.home.HomeActivity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class AppModule {

    @Binds
    abstract fun bindContext(application: Application): Context

    @ContributesAndroidInjector
    internal abstract fun contributesMainActivity(): HomeActivity

    @Binds
    abstract fun bindsPhotoRepository(photoRepositoryImpl: PhotoRepositoryImpl): PhotoRepository
}