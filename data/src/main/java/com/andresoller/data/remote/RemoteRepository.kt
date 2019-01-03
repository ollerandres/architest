package com.andresoller.data.remote

import com.andresoller.data.model.Comment
import com.andresoller.data.model.Post
import com.andresoller.data.model.User
import io.reactivex.Observable

interface RemoteRepository {

    fun getPosts(): Observable<List<Post>>

    fun getPostDetail(postId: Int): Observable<Post>

    fun getUsers(): Observable<List<User>>

    fun getPostComments(postId: Int): Observable<List<Comment>>

}