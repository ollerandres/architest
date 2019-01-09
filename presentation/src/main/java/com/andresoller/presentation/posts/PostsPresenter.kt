package com.andresoller.presentation.posts

import com.andresoller.domain.interactors.posts.PostsInteractor
import com.andresoller.presentation.posts.mapper.PostsViewStateMapper
import com.andresoller.presentation.posts.viewstates.PartialPostsViewState
import com.andresoller.presentation.posts.viewstates.PostsViewState
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class PostsPresenter @Inject constructor(private val interactor: PostsInteractor,
                                         private val mapper: PostsViewStateMapper) : CoroutineScope {

    private lateinit var job: Job
    private lateinit var view: PostsView
    override val coroutineContext: CoroutineContext
        get() = job

    fun unbind() {
        job.cancel()
    }

    fun bind(view: PostsView) {
        job = Job()
        this.view = view
        loadPosts()
    }

    private fun loadPosts() {
        launch(context = job) {
            val initialState = PostsViewState(progress = true)
            withContext(Dispatchers.Main) {
                view.render(initialState)
            }

            val partialState = try {
                val posts = interactor.getPosts()
                mapper.mapToViewState(posts)
            } catch (e: Exception) {
                PartialPostsViewState.ErrorState(e.message.toString())
            }

            val finalState = reduce(initialState, partialState)
            withContext(Dispatchers.Main) {
                view.render(finalState)
            }
        }
    }

    private fun reduce(previousState: PostsViewState, partialState: PartialPostsViewState): PostsViewState {
        return when (partialState) {
            is PartialPostsViewState.ProgressState -> PostsViewState(progress = true)
            is PartialPostsViewState.ErrorState -> PostsViewState(error = true, errorMessage = partialState.errorMessage)
            is PartialPostsViewState.PostsFetchedState -> PostsViewState(posts = partialState.posts)
        }
    }

    fun retry() {
        loadPosts()
    }
}
