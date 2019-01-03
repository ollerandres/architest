package com.andresoller.presentation.postdetails.viewstates

import com.andresoller.domain.entities.PostDetailsInfo

sealed class PartialPostDetailsViewState {

    object ProgressState : PartialPostDetailsViewState()

    class PostCollapsingState(val collapsePost: Boolean) : PartialPostDetailsViewState()

    class ErrorState(val errorMessage: String) : PartialPostDetailsViewState()

    class PostDetailsFetchedState(val postdetails: PostDetailsInfo) : PartialPostDetailsViewState()
}