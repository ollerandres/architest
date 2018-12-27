package com.andresoller.data.remote

import com.andresoller.data.model.Comment
import com.andresoller.data.model.Post
import com.andresoller.data.model.User
import com.andresoller.mlsearch.data.remote.ApiClient
import io.reactivex.Observable
import javax.inject.Inject

class RemoteRepositoryImpl @Inject constructor(private val client: ApiClient) : RemoteRepository {

    override fun getPosts(): Observable<List<Post>> {
        return client.getPosts()
    }

    override fun getPostDetail(postId: String): Observable<Post> {
        return client.getPostDetail(postId)
    }

    override fun getUsers(): Observable<List<User>> {
        return client.getUsers()
    }

    override fun getPostComments(postId: String): Observable<List<Comment>> {
        return client.getPostComments(postId)
    }
}