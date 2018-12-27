package com.andresoller.presentation.postdetail;

import com.andresoller.domain.interactors.posts.PostsInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PostDetailPresenter @Inject constructor(private val interactor: PostsInteractor,
                                              private val disposable: CompositeDisposable) {

    var view: PostDetailView? = null

    fun attachView(view: PostDetailView) {
        if (this.view == null) {
            this.view = view
        }
    }

    fun detachView() {
        view = null
    }

    fun onResume(postId: String?) {
        loadPostDetails(postId)
    }

    fun onRetry(postId: String?) {
        loadPostDetails(postId)
    }

    fun loadPostDetails(postId: String?) {
        if (postId.isNullOrEmpty()) {
            view?.displayError(view?.noResultMessage()!!)
            return
        }

        view?.startLoading()
        disposable.add(
                interactor.getPostDetail(postId)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            view?.stopLoading()
                            view?.loadPostDetails(it)
                        }, {
                            view?.displayError(it.message.toString())
                            view?.stopLoading()
                        }))
    }

    fun onCommentsScrolled(firstCompletelyVisibleItemPosition: Int) {
        if (firstCompletelyVisibleItemPosition > 0) {
            view?.collapsePost()
        } else {
            view?.expandPost()
        }
    }

    fun onPause() {
        disposable.clear()
    }
}