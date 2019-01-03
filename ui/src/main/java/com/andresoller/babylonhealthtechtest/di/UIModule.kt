package com.andresoller.babylonhealthtechtest.di

import android.content.Context
import com.andresoller.babylonhealthtechtest.BHApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UIModule(private val application: BHApplication) {

    @Provides
    @Singleton
    fun provideApplicationContext(): Context {
        return application.applicationContext
    }
}