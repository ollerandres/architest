package com.andresoller.architest

import android.app.Application
import com.andresoller.architest.di.DaggerArchitestApplicationComponent
import com.andresoller.architest.di.ArchitestApplicationComponent
import com.andresoller.architest.di.UIComponent
import com.andresoller.architest.di.UIModule
import com.andresoller.mlsearch.presentation.di.UIComponentProvider

class ArchitestApplication : Application(), UIComponentProvider {

    lateinit var component: ArchitestApplicationComponent

    override fun onCreate() {
        super.onCreate()
        component = DaggerArchitestApplicationComponent
                .builder()
                .uIModule(UIModule(this))
                .build()
    }

    override fun getUIComponent(): UIComponent {
        return component.getUIComponent()
    }
}