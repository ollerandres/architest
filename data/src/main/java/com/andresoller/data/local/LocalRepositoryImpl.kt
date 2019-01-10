package com.andresoller.data.local

import com.andresoller.data.Repository
import com.andresoller.data.model.Comment
import com.andresoller.data.model.Post
import com.andresoller.data.model.User
import io.reactivex.Observable
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(private val database: AppDatabase) : Repository {

    override fun getPosts(): Observable<ArrayList<Post>> {
        return database.postDao().getPosts()
                .map { return@map ArrayList(it) }
                .toObservable()
    }

    override fun getPostDetail(postId: String): Observable<Post> {
        return database.postDao().getPost(postId)
                .toObservable()
    }

    override fun getUsers(): Observable<ArrayList<User>> {
        return database.userDao().getUsers()
                .map { return@map ArrayList(it) }
                .toObservable()
    }

    override fun getPostComments(postId: String): Observable<ArrayList<Comment>> {
        return database.commentDao().getPostComments(postId)
                .map { return@map ArrayList(it) }
                .toObservable()
    }

    override fun savePosts(posts: ArrayList<Post>) {
        database.postDao().insertAll(posts)
    }

    override fun saveUsers(users: ArrayList<User>) {
        database.userDao().insertAll(users)
    }

    override fun saveComments(comments: ArrayList<Comment>) {
        database.commentDao().insertAll(comments)
    }
}