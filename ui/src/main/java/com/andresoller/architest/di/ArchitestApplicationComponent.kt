package com.andresoller.architest.di

import com.andresoller.data.di.DataModule
import com.andresoller.device.di.DeviceModule
import com.andresoller.domain.di.DomainModule
import com.andresoller.presentation.di.PresentationModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(UIModule::class),
    (DomainModule::class),
    (PresentationModule::class),
    (DataModule::class),
    (DeviceModule::class)])
interface ArchitestApplicationComponent {
    fun getUIComponent(): UIComponent
}