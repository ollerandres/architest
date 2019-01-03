package com.andresoller.presentation.di

import com.andresoller.domain.interactors.postdetails.PostDetailsInteractor
import com.andresoller.domain.interactors.posts.PostsInteractor
import com.andresoller.presentation.postdetails.mapper.PostDetailsViewStateMapper
import com.andresoller.presentation.postdetails.mapper.PostDetailsViewStateMapperImpl
import com.andresoller.presentation.posts.mapper.PostsViewStateMapper
import com.andresoller.presentation.posts.mapper.PostsViewStateMapperImpl
import com.andresoller.presentation.postdetails.PostDetailsPresenter
import com.andresoller.presentation.posts.PostsPresenter
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Singleton

@Module
class PresentationModule {

    @Provides
    fun providePostsViewStateMapper(): PostsViewStateMapper {
        return PostsViewStateMapperImpl()
    }

    @Provides
    fun providePostDetailsViewStateMapper(): PostDetailsViewStateMapper {
        return PostDetailsViewStateMapperImpl()
    }

    @Provides
    @Singleton
    fun providePostPresenter(postsInteractor: PostsInteractor, mapper: PostsViewStateMapper): PostsPresenter {
        return PostsPresenter(postsInteractor, CompositeDisposable(), mapper)
    }

    @Provides
    @Singleton
    fun providePostDetailPresenter(postDetailsInteractor: PostDetailsInteractor, mapper: PostDetailsViewStateMapper): PostDetailsPresenter {
        return PostDetailsPresenter(postDetailsInteractor, CompositeDisposable(), mapper)
    }
}