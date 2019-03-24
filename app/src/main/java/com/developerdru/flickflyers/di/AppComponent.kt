package com.developerdru.flickflyers.di

import android.app.Application
import com.developerdru.flickflyers.application.App
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AppModule::class,
        ViewModule::class
    ]
)
interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(app: Application): AppComponent.Builder


        fun build(): AppComponent
    }

    fun injectApp(app: App)
}