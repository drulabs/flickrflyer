package com.developerdru.flickflyers.di

import android.app.Application
import com.developerdru.flickflyers.BuildConfig
import com.developerdru.flickflyers.data.local.PhotosDAO
import com.developerdru.flickflyers.data.local.PhotosDB
import com.developerdru.flickflyers.data.remote.FlickrService
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class ViewModule {

    @Provides
    @Singleton
    fun providesPhotosDB(application: Application): PhotosDB {
        return PhotosDB.getInstance(application.applicationContext)
    }

    @Provides
    @Singleton
    fun providesPhotosDAO(database: PhotosDB): PhotosDAO {
        return database.getPhotosDAO()
    }

    @Provides
    @Singleton
    @Named("background")
    fun providesExecutionScheduler(): Scheduler {
        return Schedulers.io()
    }

    @Provides
    @Singleton
    @Named("foreground")
    fun providesPostExecutionScheduler(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    @Provides
    @Singleton
    fun providesFlickrService(retrofit: Retrofit): FlickrService {
        return retrofit.create(FlickrService::class.java)
    }

    @Provides
    @Singleton
    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .build()
    }

    // TODO add interceptors, logger, http client
}