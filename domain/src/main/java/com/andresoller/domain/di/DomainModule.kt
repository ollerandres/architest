package com.andresoller.domain.di

import com.andresoller.data.remote.RemoteRepository
import com.andresoller.domain.interactors.postdetails.PostDetailsInteractor
import com.andresoller.domain.interactors.postdetails.PostDetailsInteractorImpl
import com.andresoller.domain.interactors.posts.PostsInteractor
import com.andresoller.domain.interactors.posts.PostsInteractorImpl
import com.andresoller.domain.interactors.postdetails.mapper.PostDetailMapper
import com.andresoller.domain.interactors.postdetails.mapper.PostDetailMapperImpl
import com.andresoller.domain.interactors.posts.mapper.PostMapper
import com.andresoller.domain.interactors.posts.mapper.PostMapperImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DomainModule {

    @Provides
    @Singleton
    fun providePostMapper(): PostMapper {
        return PostMapperImpl()
    }

    @Provides
    @Singleton
    fun providePostDetailMapperImpl(): PostDetailMapper {
        return PostDetailMapperImpl()
    }

    @Provides
    @Singleton
    fun providePostsInteractor(repository: RemoteRepository, postMapper: PostMapperImpl): PostsInteractor {
        return PostsInteractorImpl(repository, postMapper)
    }

    @Provides
    @Singleton
    fun providePostDetailsInteractor(repository: RemoteRepository, postDetailMapper: PostDetailMapperImpl): PostDetailsInteractor {
        return PostDetailsInteractorImpl(repository, postDetailMapper)
    }
}