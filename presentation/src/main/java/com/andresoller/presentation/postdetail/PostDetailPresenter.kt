package com.andresoller.presentation.postdetail;

import com.andresoller.domain.entities.PostDetailsInfo
import com.andresoller.domain.error.ErrorEntity
import com.andresoller.domain.interactors.posts.PostsInteractor
import com.andresoller.domain.result.ArchitestResult
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class PostDetailPresenter @Inject constructor(
        private val interactor: PostsInteractor
) : CoroutineScope {

    var view: PostDetailView? = null
    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    fun attachView(view: PostDetailView) {
        if (this.view == null) {
            this.view = view
        }
        job = Job()
    }

    fun detachView() {
        view = null
        job.cancel()
    }

    fun onResume(postId: String?) {
        loadPostDetails(postId)
    }

    fun onRetry(postId: String?) {
        loadPostDetails(postId)
    }

    fun loadPostDetails(postId: String?) = launch {
        if (postId.isNullOrEmpty()) {
            view?.displayError(view?.noResultMessage()!!)
            cancel()
        }

        view?.startLoading()
        when (val response = interactor.getPostDetail(postId!!)) {
            is ArchitestResult.Success -> handleSuccessfulResult(response.result)
            is ArchitestResult.Error -> handleErrorResult(response.error)
        }
    }

    private fun handleSuccessfulResult(result: PostDetailsInfo) {
        view?.stopLoading()
        view?.loadPostDetails(result)
    }

    private fun handleErrorResult(error: ErrorEntity) {
        when (error) {
            is ErrorEntity.HttpError -> view?.displayError("Server Error")
            is ErrorEntity.NetworkError -> view?.displayError("No internet")
            is ErrorEntity.ParsingError -> view?.displayError("Parsing error")
            is ErrorEntity.UnknownError -> view?.displayError("Unknown error")
        }
    }

    fun onCommentsScrolled(firstCompletelyVisibleItemPosition: Int) {
        if (firstCompletelyVisibleItemPosition > 0) {
            view?.collapsePost()
        } else {
            view?.expandPost()
        }
    }
}