package com.andresoller.presentation.di

import com.andresoller.domain.interactors.posts.PostsInteractor
import com.andresoller.presentation.postdetail.PostDetailPresenter
import com.andresoller.presentation.posts.PostsPresenter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class PresentationModule {

    @Provides
    @Singleton
    fun providePostPresenter(postsInteractor: PostsInteractor): PostsPresenter {
        return PostsPresenter(postsInteractor)
    }

    @Provides
    @Singleton
    fun providePostDetailPresenter(postsInteractor: PostsInteractor): PostDetailPresenter {
        return PostDetailPresenter(postsInteractor)
    }
}