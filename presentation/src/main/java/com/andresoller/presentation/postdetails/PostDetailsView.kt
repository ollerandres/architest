package com.andresoller.presentation.postdetails;

import com.andresoller.presentation.postdetails.viewstates.PostDetailsViewState

interface PostDetailsView {

    fun render(state: PostDetailsViewState)
}