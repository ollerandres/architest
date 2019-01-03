package com.andresoller.presentation.posts.viewstates

import com.andresoller.domain.entities.PostInfo

sealed class PartialPostsViewState {

    object ProgressState : PartialPostsViewState()

    class ErrorState(val errorMessage: String) : PartialPostsViewState()

    class PostsFetchedState(val posts: ArrayList<PostInfo>) : PartialPostsViewState()
}