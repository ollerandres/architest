package com.andresoller.presentation.postdetails;

import android.util.Log
import com.andresoller.domain.interactors.postdetails.PostDetailsInteractor
import com.andresoller.presentation.postdetails.mapper.PostDetailsViewStateMapper
import com.andresoller.presentation.postdetails.viewstates.PartialPostDetailsViewState
import com.andresoller.presentation.postdetails.viewstates.PostDetailsViewState
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PostDetailsPresenter @Inject constructor(private val interactor: PostDetailsInteractor,
                                               private val disposable: CompositeDisposable,
                                               private val mapper: PostDetailsViewStateMapper) {

    var view: PostDetailsView? = null

    fun attachView(view: PostDetailsView) {
        if (this.view == null) {
            this.view = view
        }
    }

    fun onPause() {
        view = null
        disposable.clear()
    }

    fun bindIntents(view: PostDetailsView) {
        attachView(view)

        val pullToRefresh = view.pullToRefreshIntent()
                .flatMap {
                    return@flatMap interactor.getPostDetail(it)
                            .subscribeOn(Schedulers.io())
                            .map { return@map mapper.mapToViewState(it) }
                            .onErrorReturn { throwable -> PartialPostDetailsViewState.ErrorState(throwable.message.toString()) }
                }

        val postIdIntent = view.postIdExtraIntent()
                .flatMap {
                    interactor.getPostDetail(it)
                            .subscribeOn(Schedulers.io())
                            .map { return@map mapper.mapToViewState(it) }
                            .onErrorReturn { throwable -> PartialPostDetailsViewState.ErrorState(throwable.message.toString()) }
                }

        val scrolledRecyclerViewIntent = view.scrolledRecyclerViewIntent()
                .flatMap { Observable.just(PartialPostDetailsViewState.PostCollapsingState(it)) }

        val allIntentsObservable = Observable.merge(listOf(postIdIntent, pullToRefresh, scrolledRecyclerViewIntent)).observeOn(AndroidSchedulers.mainThread())

        val initialState = PostDetailsViewState(progress = true)

        disposable.add(
                allIntentsObservable.scan(initialState, this::reduce)
                        .subscribe { view.render(it) }
        )
    }

    private fun reduce(previousState: PostDetailsViewState, partialState: PartialPostDetailsViewState): PostDetailsViewState {
        return when (partialState) {
            is PartialPostDetailsViewState.ProgressState -> PostDetailsViewState(progress = true)
            is PartialPostDetailsViewState.ErrorState -> PostDetailsViewState(error = true, errorMessage = partialState.errorMessage)
            is PartialPostDetailsViewState.PostDetailsFetchedState -> PostDetailsViewState(collapsePost = previousState.collapsePost, postDetails = partialState.postdetails)
            is PartialPostDetailsViewState.PostCollapsingState -> PostDetailsViewState(collapsePost = partialState.collapsePost, postDetails = previousState.postDetails)
        }
    }
}