package com.andresoller.data.remote

import com.andresoller.data.model.Comment
import com.andresoller.data.model.Post
import com.andresoller.data.model.User
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiClient {

    @GET("posts")
    fun getPosts(): Deferred<List<Post>>

    @GET("users")
    fun getUsers(): Deferred<List<User>>

    @GET("comments")
    fun getPostComments(@Query("postId") postId: Int): Deferred<List<Comment>>

    @GET("posts/{postId}")
    fun getPostDetail(@Path("postId") postId: Int): Deferred<Post>
}