package com.andresoller.presentation.posts

import com.andresoller.domain.entities.PostInfo

interface PostsView {

    fun loadPosts(posts: ArrayList<PostInfo>)

    fun displayErrorBanner(message: String)

    fun startLoading()

    fun stopLoading()

    fun displayNoResultsError(noResultMessage: String)

    fun clearPosts()

    fun noResultMessage(): String
}