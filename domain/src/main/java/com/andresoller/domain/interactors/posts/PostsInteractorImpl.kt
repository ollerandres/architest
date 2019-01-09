package com.andresoller.domain.interactors.posts;

import com.andresoller.data.remote.RemoteRepository
import com.andresoller.domain.entities.PostInfo
import com.andresoller.domain.interactors.posts.mapper.PostMapper
import kotlinx.coroutines.Deferred
import javax.inject.Inject

class PostsInteractorImpl @Inject constructor(private val repository: RemoteRepository,
                                              private val postMapper: PostMapper) : PostsInteractor {

    override suspend fun getPosts(): ArrayList<PostInfo> {
        val posts = repository.getPosts().await()
        val users = repository.getUsers().await()
        return postMapper.mapToEntity(posts, users)
    }
}
