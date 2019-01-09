package com.andresoller.domain.interactors.postdetails

import com.andresoller.data.remote.RemoteRepository
import com.andresoller.domain.entities.PostDetailsInfo
import com.andresoller.domain.interactors.postdetails.mapper.PostDetailMapper
import javax.inject.Inject

class PostDetailsInteractorImpl @Inject constructor(private val repository: RemoteRepository,
                                                    private val mapper: PostDetailMapper) : PostDetailsInteractor {

    override suspend fun getPostDetail(postId: Int): PostDetailsInfo {
        val post = repository.getPostDetail(postId).await()
        val users = repository.getUsers().await()
        val comments = repository.getPostComments(postId).await()
        return mapper.mapToEntity(post, users, comments)
    }
}