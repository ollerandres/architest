package com.andresoller.presentation.posts

import com.andresoller.presentation.posts.viewstates.PostsViewState
import io.reactivex.Observable

interface PostsView {

    fun render(state: PostsViewState)

    fun pullToRefreshIntent(): Observable<Boolean>

    fun loadPostsIntent(): Observable<Boolean>
}