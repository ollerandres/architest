package com.andresoller.architest

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ArchitestApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}