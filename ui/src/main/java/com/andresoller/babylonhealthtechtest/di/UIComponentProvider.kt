package com.andresoller.mlsearch.presentation.di

import com.andresoller.babylonhealthtechtest.di.UIComponent

interface UIComponentProvider {

    fun getUIComponent(): UIComponent
}