package com.andresoller.device.di

import android.content.Context
import com.andresoller.device.utils.NetworkUtils
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DeviceModule() {

    @Provides
    @Singleton
    fun provideNetworkUtils(context: Context): NetworkUtils {
        return NetworkUtils(context)
    }
}