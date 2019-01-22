package com.andresoller.presentation.posts

import com.andresoller.domain.interactors.posts.PostsInteractor
import com.andresoller.presentation.posts.mapper.PostsViewStateMapper
import com.andresoller.presentation.posts.viewstates.PartialPostsViewState
import com.andresoller.presentation.posts.viewstates.PostsViewState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class PostsPresenter @Inject constructor(private val interactor: PostsInteractor,
                                         private val mapper: PostsViewStateMapper) : CoroutineScope {

    private lateinit var job: Job
    private lateinit var view: PostsView
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    fun unbind() {
        job.cancel()
    }

    fun bindIntents(view: PostsView) {
        job = Job()
        this.view = view
        loadPosts()
    }

    private fun loadPosts() {
        launch(context = job) {
            while (view.loadDataIntent().receive()) {
                val initialState = PostsViewState(progress = true)
                view.render(initialState)
                val partialState = try {
                    val posts = interactor.getPosts()
                    mapper.mapToViewState(posts)
                } catch (e: Exception) {
                    PartialPostsViewState.ErrorState(e.message.toString())
                }

                val finalState = reduce(initialState, partialState)
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
}
