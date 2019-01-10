package com.andresoller.mlsearch.presentation.di

import com.andresoller.architest.di.UIComponent

interface UIComponentProvider {

    fun getUIComponent(): UIComponent
}