package com.andresoller.babylonhealthtechtest.di

import com.andresoller.babylonhealthtechtest.activities.PostDetailsActivity
import com.andresoller.babylonhealthtechtest.activities.PostsActivity
import dagger.Subcomponent

@Subcomponent
interface UIComponent {

    fun inject(activity: PostsActivity)

    fun inject(activity: PostDetailsActivity)
}
