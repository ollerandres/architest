package com.andresoller.babylonhealthtechtest

import android.app.Application
import com.andresoller.babylonhealthtechtest.di.BHApplicationComponent
import com.andresoller.babylonhealthtechtest.di.DaggerBHApplicationComponent
import com.andresoller.babylonhealthtechtest.di.UIComponent
import com.andresoller.babylonhealthtechtest.di.UIModule
import com.andresoller.mlsearch.presentation.di.UIComponentProvider

class BHApplication : Application(), UIComponentProvider {

    lateinit var component: BHApplicationComponent

    override fun onCreate() {
        super.onCreate()
        component = DaggerBHApplicationComponent
                .builder()
                .uIModule(UIModule(this))
                .build()
    }

    override fun getUIComponent(): UIComponent {
        return component.getUIComponent()
    }
}