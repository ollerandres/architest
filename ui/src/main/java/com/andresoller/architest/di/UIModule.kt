package com.andresoller.architest.di

import com.andresoller.architest.ArchitestApplication
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
class UIModule(private val application: ArchitestApplication) {

    // No-op
}