package com.andresoller.presentation.postdetails;

import com.andresoller.domain.interactors.postdetails.PostDetailsInteractor
import com.andresoller.presentation.postdetails.mapper.PostDetailsViewStateMapper
import com.andresoller.presentation.postdetails.viewstates.PartialPostDetailsViewState
import com.andresoller.presentation.postdetails.viewstates.PostDetailsViewState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class PostDetailsPresenter @Inject constructor(private val interactor: PostDetailsInteractor,
                                               private val mapper: PostDetailsViewStateMapper) : CoroutineScope {

    private lateinit var job: Job
    private lateinit var view: PostDetailsView
    private lateinit var actualViewState: PostDetailsViewState
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    fun onPause() {
        job.cancel()
    }

    fun bindIntents(view: PostDetailsView) {
        job = Job()
        this.view = view
        loadPostDetails()
        scrolledRecyclerView()
    }

    fun loadPostDetails() {
        launch(context = job) {
            while (view.loadDataIntent().receive()) {
                actualViewState = PostDetailsViewState(progress = true)
                view.render(actualViewState)

                val partialState = try {
                    val posts = interactor.getPostDetail(view.postIdChannel().receive())
                    mapper.mapToViewState(posts)
                } catch (e: Exception) {
                    PartialPostDetailsViewState.ErrorState(e.message.toString())
                }

                val finalState = reduce(partialState)
                view.render(finalState)
            }
        }
    }

    fun scrolledRecyclerView() {
        launch(context = job) {
            while (view.onScrollIntent().receive()) {
                val reducedState = reduce(PartialPostDetailsViewState.PostCollapsingState(view.collapseStateChannel().receive()))
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
}