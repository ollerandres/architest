package com.andresoller.domain.repositories

import com.andresoller.domain.entities.PostDetailsInfo
import com.andresoller.domain.entities.PostInfo
import com.andresoller.domain.result.ArchitestResult

interface RemoteRepository {

    suspend fun getPosts(): ArchitestResult<List<PostInfo>>

    suspend fun getPostDetail(postId: String): ArchitestResult<PostDetailsInfo>
}