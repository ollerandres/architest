package com.andresoller.architest.di

import com.andresoller.architest.activities.PostDetailActivity
import com.andresoller.architest.activities.PostsActivity
import dagger.Subcomponent

@Subcomponent
interface UIComponent {

    fun inject(activity: PostsActivity)

    fun inject(activity: PostDetailActivity)
}
