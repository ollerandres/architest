package com.andresoller.data.remote

import com.andresoller.data.model.Comment
import com.andresoller.data.model.Post
import com.andresoller.data.model.User
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiClient {

    @GET("posts")
    fun getPosts(): Observable<ArrayList<Post>>

    @GET("users")
    fun getUsers(): Observable<ArrayList<User>>

    @GET("comments")
    fun getPostComments(@Query("postId") postId: String): Observable<ArrayList<Comment>>

    @GET("posts/{postId}")
    fun getPostDetail(@Path("postId") postId: String): Observable<Post>
}