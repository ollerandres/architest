package com.andresoller.domain.interactors.posts;

import com.andresoller.domain.entities.PostDetailsInfo
import com.andresoller.domain.entities.PostInfo
import com.andresoller.domain.repositories.RemoteRepository
import com.andresoller.domain.result.ArchitestResult
import javax.inject.Inject

class PostsInteractorImpl @Inject constructor(
        private val repository: RemoteRepository
) : PostsInteractor {

    override suspend fun getPosts(): ArchitestResult<List<PostInfo>> {
        return repository.getPosts()
    }

    override suspend fun getPostDetail(postId: String): ArchitestResult<PostDetailsInfo> {
        return repository.getPostDetail(postId)
    }
}
