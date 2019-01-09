package com.andresoller.data.remote

import com.andresoller.data.model.Comment
import com.andresoller.data.model.Post
import com.andresoller.data.model.User
import kotlinx.coroutines.Deferred

interface RemoteRepository {

    fun getPosts(): Deferred<List<Post>>

    fun getPostDetail(postId: Int): Deferred<Post>

    fun getUsers(): Deferred<List<User>>

    fun getPostComments(postId: Int): Deferred<List<Comment>>

}