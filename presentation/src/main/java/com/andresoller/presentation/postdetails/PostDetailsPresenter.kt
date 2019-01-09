package com.andresoller.presentation.postdetails;

import com.andresoller.domain.interactors.postdetails.PostDetailsInteractor
import com.andresoller.presentation.postdetails.mapper.PostDetailsViewStateMapper
import com.andresoller.presentation.postdetails.viewstates.PartialPostDetailsViewState
import com.andresoller.presentation.postdetails.viewstates.PostDetailsViewState
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class PostDetailsPresenter @Inject constructor(private val interactor: PostDetailsInteractor,
                                               private val mapper: PostDetailsViewStateMapper) : CoroutineScope {

    private lateinit var job: Job
    private lateinit var view: PostDetailsView
    private lateinit var actualViewState: PostDetailsViewState
    override val coroutineContext: CoroutineContext
        get() = job

    fun onPause() {
        job.cancel()
    }

    fun bind(view: PostDetailsView, postId: Int) {
        job = Job()
        this.view = view
        loadPostDetails(postId)
    }

    fun loadPostDetails(postId: Int) {
        launch(context = job) {
            actualViewState = PostDetailsViewState(progress = true)
            withContext(Dispatchers.Main) {
                view.render(actualViewState)
            }

            val partialState = try {
                val posts = interactor.getPostDetail(postId)
                mapper.mapToViewState(posts)
            } catch (e: Exception) {
                PartialPostDetailsViewState.ErrorState(e.message.toString())
            }

            val finalState = reduce(partialState)
            withContext(Dispatchers.Main) {
                view.render(finalState)
            }
        }
    }

    fun scrolledRecyclerView(firstItemVisible: Boolean) {
        launch(context = job) {
            withContext(Dispatchers.Main) {
                val reducedState = reduce(PartialPostDetailsViewState.PostCollapsingState(firstItemVisible))
                view.render(reducedState)
            }
        }
    }

    private fun reduce(partialState: PartialPostDetailsViewState): PostDetailsViewState {
        actualViewState = when (partialState) {
            is PartialPostDetailsViewState.ProgressState -> PostDetailsViewState(progress = true)
            is PartialPostDetailsViewState.ErrorState -> PostDetailsViewState(error = true, errorMessage = partialState.errorMessage)
            is PartialPostDetailsViewState.PostDetailsFetchedState -> PostDetailsViewState(collapsePost = actualViewState.collapsePost, postDetails = partialState.postdetails)
            is PartialPostDetailsViewState.PostCollapsingState -> PostDetailsViewState(collapsePost = partialState.collapsePost, postDetails = actualViewState.postDetails)
        }
        return actualViewState
    }

    fun refresh(postId: Int) {
        loadPostDetails(postId)
    }
}