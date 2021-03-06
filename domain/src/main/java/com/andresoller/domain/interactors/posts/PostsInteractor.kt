package com.andresoller.domain.interactors.posts

import com.andresoller.domain.entities.PostDetailsInfo
import com.andresoller.domain.entities.PostInfo
import com.andresoller.domain.result.ArchitestResult

interface PostsInteractor {

    suspend fun getPosts(): ArchitestResult<List<PostInfo>>

    suspend fun getPostDetail(postId: String): ArchitestResult<PostDetailsInfo>
}