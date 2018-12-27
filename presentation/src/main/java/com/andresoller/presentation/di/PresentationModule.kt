package com.andresoller.presentation.di

import com.andresoller.domain.interactors.posts.PostsInteractor
import com.andresoller.presentation.postdetail.PostDetailPresenter
import com.andresoller.presentation.posts.PostsPresenter
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Singleton

@Module
class PresentationModule {

    @Provides
    @Singleton
    fun providePostPresenter(postsInteractor: PostsInteractor): PostsPresenter {
        return PostsPresenter(postsInteractor, CompositeDisposable())
    }

    @Provides
    @Singleton
    fun providePostDetailPresenter(postsInteractor: PostsInteractor): PostDetailPresenter {
        return PostDetailPresenter(postsInteractor, CompositeDisposable())
    }
}