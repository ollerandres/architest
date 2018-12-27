package com.andresoller.babylonhealthtechtest.di

import com.andresoller.babylonhealthtechtest.activities.PostDetailActivity
import com.andresoller.babylonhealthtechtest.activities.PostsActivity
import dagger.Subcomponent

@Subcomponent
interface UIComponent {

    fun inject(activity: PostsActivity)

    fun inject(activity: PostDetailActivity)
}
