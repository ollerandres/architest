package com.andresoller.data

import com.andresoller.data.model.Comment
import com.andresoller.data.model.Post
import com.andresoller.data.model.User
import io.reactivex.Observable

interface Repository {

    fun getPosts(): Observable<ArrayList<Post>>

    fun getPostDetail(postId: String): Observable<Post>

    fun getUsers(): Observable<ArrayList<User>>

    fun getPostComments(postId: String): Observable<ArrayList<Comment>>

    fun savePosts(posts: ArrayList<Post>)

    fun saveUsers(users: ArrayList<User>)

    fun saveComments(comments: ArrayList<Comment>)
}