package com.andresoller.presentation.posts

import com.andresoller.presentation.posts.viewstates.PostsViewState

interface PostsView {

    fun render(state: PostsViewState)
}