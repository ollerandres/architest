package com.andresoller.presentation.postdetail;

import com.andresoller.domain.entities.PostDetailsInfo

interface PostDetailView {

    fun startLoading()

    fun stopLoading()

    fun loadPostDetails(postDetailsInfo: PostDetailsInfo)

    fun noResultMessage(): String

    fun displayError(message: String)

    fun collapsePost()

    fun expandPost()
}