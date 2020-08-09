package com.andresoller.data.remote

import com.andresoller.data.mappers.postdetail.PostDetailMapper
import com.andresoller.data.mappers.posts.PostMapper
import com.andresoller.data.remote.errors.SafeFunctionExecutor
import com.andresoller.domain.entities.PostDetailsInfo
import com.andresoller.domain.entities.PostInfo
import com.andresoller.domain.repositories.RemoteRepository
import com.andresoller.domain.result.ArchitestResult
import javax.inject.Inject

class RemoteRepositoryImpl @Inject constructor(
        private val client: ApiClient,
        private val safeFunctionExecutor: SafeFunctionExecutor,
        private val postMapper: PostMapper,
        private val postDetailMapper: PostDetailMapper
) : RemoteRepository {

    override suspend fun getPosts(): ArchitestResult<List<PostInfo>> {
        return safeFunctionExecutor.executeSafeFunction {
            postMapper.mapToEntity(client.getPosts(), client.getUsers())
        }
    }

    override suspend fun getPostDetail(postId: String): ArchitestResult<PostDetailsInfo> {
        return safeFunctionExecutor.executeSafeFunction {
            postDetailMapper.mapToEntity(client.getPostDetail(postId), client.getUsers(), client.getPostComments(postId))
        }
    }
}