package com.andresoller.presentation.posts

import com.andresoller.domain.entities.PostInfo
import com.andresoller.domain.error.ErrorEntity
import com.andresoller.domain.interactors.posts.PostsInteractor
import com.andresoller.domain.result.ArchitestResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class PostsPresenter @Inject constructor(
        private val postsInteractor: PostsInteractor
) : CoroutineScope {

    var view: PostsView? = null
    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    fun attachView(view: PostsView) {
        if (this.view == null) {
            this.view = view
        }
        job = Job()
    }

    fun detachView() {
        view = null
        job.cancel()
    }

    fun onResume() {
        loadPosts()
    }

    fun onRetry() {
        view?.clearPosts()
        loadPosts()
    }

    fun loadPosts() = launch {
        view?.startLoading()
        when (val response = postsInteractor.getPosts()) {
            is ArchitestResult.Success -> handleSuccessfulResult(response.result)
            is ArchitestResult.Error -> handleErrorResult(response.error)
        }
    }

    private fun handleSuccessfulResult(result: List<PostInfo>) {
        view?.stopLoading()
        if (result.isNotEmpty()) {
            view?.loadPosts(result)
        } else {
            view?.displayNoResultsError(view?.noResultMessage()!!)
        }
    }

    private fun handleErrorResult(error: ErrorEntity) {
        view?.stopLoading()
        when (error) {
            is ErrorEntity.HttpError -> view?.displayErrorBanner("Server Error")
            is ErrorEntity.NetworkError -> view?.displayErrorBanner("No internet")
            is ErrorEntity.ParsingError -> view?.displayErrorBanner("Parsing error")
            is ErrorEntity.UnknownError -> view?.displayErrorBanner("Unknown error")
        }
    }
}
