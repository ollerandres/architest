package com.andresoller.data.remote

import com.andresoller.data.Repository
import com.andresoller.data.model.Comment
import com.andresoller.data.model.Post
import com.andresoller.data.model.User
import io.reactivex.Observable
import javax.inject.Inject

class RemoteRepositoryImpl @Inject constructor(private val client: ApiClient) : Repository {

    override fun getPosts(): Observable<ArrayList<Post>> {
        return client.getPosts()
    }

    override fun getPostDetail(postId: String): Observable<Post> {
        return client.getPostDetail(postId)
    }

    override fun getUsers(): Observable<ArrayList<User>> {
        return client.getUsers()
    }

    override fun getPostComments(postId: String): Observable<ArrayList<Comment>> {
        return client.getPostComments(postId)
    }

    override fun savePosts(posts: ArrayList<Post>) {
    }

    override fun saveUsers(users: ArrayList<User>) {
    }

    override fun saveComments(comments: ArrayList<Comment>) {
    }
}