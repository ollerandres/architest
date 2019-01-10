package com.andresoller.presentation.di

import com.andresoller.domain.interactors.posts.PostsInteractor
import com.andresoller.presentation.postdetail.PostDetailsViewModel
import com.andresoller.presentation.postdetail.mapper.PostDetailsViewStateMapper
import com.andresoller.presentation.postdetail.mapper.PostDetailsViewStateMapperImpl
import com.andresoller.presentation.posts.PostsViewModel
import com.andresoller.presentation.posts.PostsViewModelFactory
import com.andresoller.presentation.posts.mapper.PostsViewStateMapper
import com.andresoller.presentation.posts.mapper.PostsViewStateMapperImpl
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
    fun providePostsViewModelFactory(interactor: PostsInteractor, mapper: PostsViewStateMapper): PostsViewModelFactory {
        return PostsViewModelFactory(PostsViewModel(interactor, mapper, CompositeDisposable()))
    }

    @Provides
    @Singleton
    fun providePostDetailPresenter(postsInteractor: PostsInteractor, mapper: PostDetailsViewStateMapper): PostDetailsViewModel {
        return PostDetailsViewModel(postsInteractor, mapper, CompositeDisposable())
    }
}