package com.andresoller.data.remote

import com.andresoller.data.model.Comment
import com.andresoller.data.model.Post
import com.andresoller.data.model.User
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiClient {

    @GET("posts")
    suspend fun getPosts(): List<Post>

    @GET("users")
    suspend fun getUsers(): List<User>

    @GET("comments")
    suspend fun getPostComments(@Query("postId") postId: String): List<Comment>

    @GET("posts/{postId}")
    suspend fun getPostDetail(@Path("postId") postId: String): Post
}