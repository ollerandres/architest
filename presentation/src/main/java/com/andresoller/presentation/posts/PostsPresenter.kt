package com.andresoller.presentation.posts

import android.util.Log
import com.andresoller.domain.interactors.posts.PostsInteractor
import com.andresoller.presentation.posts.mapper.PostsViewStateMapper
import com.andresoller.presentation.posts.viewstates.PartialPostsViewState
import com.andresoller.presentation.posts.viewstates.PostsViewState
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PostsPresenter @Inject constructor(private val interactor: PostsInteractor,
                                         private val disposable: CompositeDisposable,
                                         private val mapper: PostsViewStateMapper) {

    var view: PostsView? = null

    fun attachView(view: PostsView) {
        if (this.view == null) {
            this.view = view
        }
    }

    fun bindIntents(view: PostsView) {
        attachView(view)
        val pullToRefresh = view.pullToRefreshIntent()
                .flatMap {
                    return@flatMap interactor.getPosts()
                            .subscribeOn(Schedulers.io())
                            .map { return@map mapper.mapToViewState(it) }
                            .onErrorReturn { throwable -> PartialPostsViewState.ErrorState(throwable.message.toString()) }
                }

        val postsLoaded = view.loadPostsIntent()
                .flatMap {
                    return@flatMap interactor.getPosts()
                            .subscribeOn(Schedulers.io())
                            .map { return@map mapper.mapToViewState(it) }
                            .onErrorReturn { throwable -> PartialPostsViewState.ErrorState(throwable.message.toString()) }
                }

        val allIntentsObservable = Observable.merge(listOf(postsLoaded, pullToRefresh)).observeOn(AndroidSchedulers.mainThread())

        val initialState = PostsViewState(progress = true)

        disposable.add(
                allIntentsObservable.scan(initialState, this::reduce)
                        .subscribe { view.render(it) }
        )
    }

    fun onPause() {
        view = null
        disposable.clear()
    }

    private fun reduce(previousState: PostsViewState, partialState: PartialPostsViewState): PostsViewState {
        return when (partialState) {
            is PartialPostsViewState.ProgressState -> PostsViewState(progress = true)
            is PartialPostsViewState.ErrorState -> PostsViewState(error = true, errorMessage = partialState.errorMessage)
            is PartialPostsViewState.PostsFetchedState -> PostsViewState(posts = partialState.posts)
        }
    }
}
