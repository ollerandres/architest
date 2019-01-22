package com.andresoller.presentation.postdetails;

import com.andresoller.presentation.postdetails.viewstates.PostDetailsViewState
import kotlinx.coroutines.channels.Channel

interface PostDetailsView {

    fun onScrollIntent(): Channel<Boolean>

    fun loadDataIntent(): Channel<Boolean>

    fun collapseStateChannel(): Channel<Boolean>

    fun postIdChannel(): Channel<Int>

    fun render(state: PostDetailsViewState)
}