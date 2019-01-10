package com.andresoller.architest.di

import android.content.Context
import com.andresoller.architest.ArchitestApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UIModule(private val application: ArchitestApplication) {

    @Provides
    @Singleton
    fun provideApplicationContext(): Context {
        return application.applicationContext
    }
}