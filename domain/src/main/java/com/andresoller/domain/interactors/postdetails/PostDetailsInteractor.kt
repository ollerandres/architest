package com.andresoller.domain.interactors.postdetails

import com.andresoller.domain.entities.PostDetailsInfo

interface PostDetailsInteractor {

    suspend fun getPostDetail(postId: Int): PostDetailsInfo
}