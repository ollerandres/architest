package com.andresoller.presentation.posts

import com.andresoller.presentation.posts.viewstates.PostsViewState
import kotlinx.coroutines.channels.Channel

interface PostsView {

    fun loadDataIntent(): Channel<Boolean>

    fun render(state: PostsViewState)
}