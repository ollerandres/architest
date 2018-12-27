package com.andresoller.presentation.posts

import com.andresoller.domain.interactors.posts.PostsInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PostsPresenter @Inject constructor(private val postsInteractor: PostsInteractor,
                                         private val disposable: CompositeDisposable) {

    var view: PostsView? = null

    fun attachView(view: PostsView) {
        if (this.view == null) {
            this.view = view
        }
    }

    fun detachView() {
        view = null
    }

    fun onResume() {
        loadPosts()
    }

    fun onRetry() {
        view?.clearPosts()
        loadPosts()
    }

    fun loadPosts() {
        view?.startLoading()
        disposable.add(
                postsInteractor.getPosts()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            view?.stopLoading()
                            if (it.isEmpty()) {
                                view?.displayNoResultsError(view?.noResultMessage()!!)
                            } else {
                                view?.loadPosts(it)
                            }
                        }, {
                            view?.displayErrorBanner(it.message.toString())
                            view?.stopLoading()
                        }))
    }

    fun onPause() {
        disposable.clear()
    }
}
