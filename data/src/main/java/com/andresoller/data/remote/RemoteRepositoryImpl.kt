package com.andresoller.data.remote

import com.andresoller.data.model.Comment
import com.andresoller.data.model.Post
import com.andresoller.data.model.User
import kotlinx.coroutines.Deferred
import javax.inject.Inject

class RemoteRepositoryImpl @Inject constructor(private val client: ApiClient) : RemoteRepository {

    override fun getPosts(): Deferred<List<Post>> {
        return client.getPosts()
    }

    override fun getPostDetail(postId: Int): Deferred<Post> {
        return client.getPostDetail(postId)
    }

    override fun getUsers(): Deferred<List<User>> {
        return client.getUsers()
    }

    override fun getPostComments(postId: Int): Deferred<List<Comment>> {
        return client.getPostComments(postId)
    }
}