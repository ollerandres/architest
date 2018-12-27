package com.andresoller.domain.di

import com.andresoller.data.remote.RemoteRepository
import com.andresoller.domain.interactors.posts.PostsInteractor
import com.andresoller.domain.interactors.posts.PostsInteractorImpl
import com.andresoller.domain.mappers.postdetail.PostDetailMapper
import com.andresoller.domain.mappers.postdetail.PostDetailMapperImpl
import com.andresoller.domain.mappers.posts.PostMapper
import com.andresoller.domain.mappers.posts.PostMapperImpl
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
    fun providePostsInteractor(repository: RemoteRepository, postMapper: PostMapperImpl, postDetailMapper: PostDetailMapperImpl): PostsInteractor {
        return PostsInteractorImpl(repository, postMapper, postDetailMapper)
    }
}