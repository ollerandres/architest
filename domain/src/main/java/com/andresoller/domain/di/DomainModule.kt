package com.andresoller.domain.di

import com.andresoller.domain.interactors.posts.PostsInteractor
import com.andresoller.domain.interactors.posts.PostsInteractorImpl
import com.andresoller.domain.repositories.RemoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class DomainModule {

    @Provides
    @Singleton
    fun providePostsInteractor(repository: RemoteRepository): PostsInteractor {
        return PostsInteractorImpl(repository)
    }
}